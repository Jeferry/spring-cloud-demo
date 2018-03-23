package com.example.demo.consumer.service.collapse;

import com.example.demo.consumer.service.UserAnnotationService;
import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author mao
 */
public class UserCollapseCommand extends HystrixCollapser<List<UserVO>, UserVO, Long> {

    private UserAnnotationService userAnnotationService;
    private Long id;

    /**
     * https://github.com/Netflix/Hystrix/wiki/How-To-Use#Collapsing
     *
     * @param userAnnotationService
     * @param id
     */
    public UserCollapseCommand(UserAnnotationService userAnnotationService, Long id) {
        // Setter 初始化
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("userCollapseCommand"))
                .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter()
                        // 设置合并批处理请求允许的最大请求数
                        .withMaxRequestsInBatch(Integer.MAX_VALUE)
                        // 设置合并请求的延迟为10ms——默认10ms
                        .withTimerDelayInMilliseconds(10)
                        // 开启缓存请求——默认true
                        .withRequestCacheEnabled(true)

                ));
        this.userAnnotationService = userAnnotationService;
        this.id = id;
    }

    /**
     * 返回给定的单个请求参数 id
     *
     * @return
     */
    @Override
    public Long getRequestArgument() {
        return id;
    }

    /**
     * 请求合并器的核心（1/2）
     * collapsedRequests 保存了延迟时间窗口中收集到的所有单个 UserVO 的请求，合并成一个 new UserBatchCommand()
     *
     * @param collapsedRequests
     * @return
     */
    @Override
    protected HystrixCommand<List<UserVO>> createCommand(Collection<CollapsedRequest<UserVO, Long>> collapsedRequests) {
        List<Long> ids = collapsedRequests.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList());
        return new UserBatchCommand(userAnnotationService, ids);
    }

    /**
     * 请求合并器的核心（2/2）
     * 在批量请求命令 UserBatchCommand 实例被触发执行完成之后执行，batchResponse 保存了 createCommand 中组织批量请求命令的返回结果。
     * collapsedRequests 代表了每个被合并的请求
     *
     * @param batchResponse
     * @param collapsedRequests
     */
    @Override
    protected void mapResponseToRequests(List<UserVO> batchResponse, Collection<CollapsedRequest<UserVO, Long>> collapsedRequests) {
        AtomicInteger count = new AtomicInteger();
        collapsedRequests.forEach(collapsedRequest -> collapsedRequest.setResponse(batchResponse.get(count.getAndIncrement())));
    }
}
