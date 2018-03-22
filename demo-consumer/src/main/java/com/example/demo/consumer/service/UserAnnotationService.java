/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.service;

import com.example.demo.commons.exception.DemoException;
import com.example.demo.modules.UserVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author maojifeng
 * @version UserAnnotationService.java, v 0.1 maojifeng
 * @date 2018/3/19 15:41
 * @comment 通过@HystrixAnnotation注解的实现
 */
@Service
public class UserAnnotationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 同步方法
     * 如果定义了 cacheKeyMethod 则 CacheKey 注解不生效
     *
     * @param id
     * @return
     */
    @CacheResult(cacheKeyMethod = "getUserCacheKey")
    @HystrixCommand(groupKey = "ribbon-annotation", commandKey = "getUserByIdSync", threadPoolKey = "getUserByIdSyncThread",
            fallbackMethod = "defaultUserVO")
    public UserVO getUserByIdSync(Long id) {
        return wrapperRequest(id);
    }

    /**
     * 异步方法
     * 异常传播：
     * 除了 HystrixBadRequestException 之外，其余异常均会被 Hystrix 认为命令执行失败，从而触发服务降级
     * ignoreExceptions 的实现方式为：当 getUserByIdAsync 方法抛出类型为 DemoException 的异常时，Hystrix
     * 会将它包装在 HystrixBadRequestException 中抛出，从而实现异常忽略，不触发后续的fallback
     *
     * @param id
     * @return
     */
    @CacheResult
    @HystrixCommand(ignoreExceptions = {DemoException.class})
    public Future<UserVO> getUserByIdAsync(@CacheKey final Long id) {
        return new AsyncResult<UserVO>() {
            @Override
            public UserVO invoke() {
                return wrapperRequest(id);
            }
        };
    }

    /**
     * 注解式响应式命令
     * EAGER表示使用toObserve()执行方式 hot observable
     * Lazy表示使用toObservable()执行方式 cold observable
     *
     * @param id
     * @return
     */
    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER, defaultFallback = "defaultUserVO")
//    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.LAZY)
    public Observable<UserVO> getUserObservableById(final Long id) {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    UserVO userVO = wrapperRequest(id);
                    subscriber.onNext(userVO);
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    /**
     * 原生实现，查询单个，任何注解
     *
     * @param id
     * @return
     */
    @HystrixCollapser(batchMethod = "findFutureAll", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public Future<UserVO> find(Long id) {
        return null;
    }

    /**
     * 原生实现，批量查询，无任何注解
     *
     * @param ids
     * @return
     */
    public List<UserVO> findAll(List<Long> ids) {
        return wrapperRequestForList(ids);
    }

    @HystrixCommand
    public List<UserVO> findFutureAll(List<Long> ids) {
        return wrapperRequestForList(ids);
    }

    /**
     * 请求单个 UserVO 包装方法
     *
     * @param id
     * @return
     */
    private UserVO wrapperRequest(final Long id) {
        return restTemplate.getForObject("http://HELLO-SERVICE/users/{id}", UserVO.class, id);

    }

    /**
     * 批量请求 List<UserVO> 包装方法
     *
     * @param ids
     * @return
     */
    private List<UserVO> wrapperRequestForList(final List<Long> ids) {
        return restTemplate.getForObject("http://HELLO-SERVICE/users?ids={ids}", List.class, StringUtils.join(ids, ","));

    }

    /**
     * 服务降级实现
     * 也有可能再次失败，所以定义defaultUserVOSec
     *
     * @param id fallback方法入参必须包含原方法的所有入参
     * @return
     */
    @HystrixCommand(defaultFallback = "defaultUserVOSec")
    private UserVO defaultUserVO(Long id) {
        UserVO userVO = new UserVO();
        userVO.setId(-2L);
        userVO.setRegistrationTime(new Date());
        return userVO;
    }

    /**
     * 在 fallback 方法中定义 Throwable 即可获取触发降级服务的异常
     *
     * @param id
     * @param throwable
     * @return
     */
    private UserVO defaultUserVOSec(Long id, Throwable throwable) {
        logger.error("error happened:", throwable);
        UserVO userVO = new UserVO();
        userVO.setId(-3L);
        userVO.setRegistrationTime(new Date());
        return userVO;
    }

    /**
     * 自定义cache key
     *
     * @param id
     * @return CacheKey must String
     */
    private String getUserCacheKey(Long id) {
        return String.valueOf(id);
    }

    /**
     * remove cache
     *
     * @param id
     */
    @CacheRemove(commandKey = "getUserByIdAsync")
    @HystrixCommand
    public void removeCache(@CacheKey Long id) {
        //if update something should remove cache key
    }

}
