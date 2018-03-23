/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.service;

import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @author maojifeng
 * @version UserCommand.java, v 0.1 maojifeng
 * @date 2018/3/19 11:13
 * @comment 用户请求封装-只能发射一次
 */
public class UserCommand extends HystrixCommand<UserVO> {

    /**
     * 缓存 HystrixCommandKey
     */
    private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("userGetCommandKey");
    private RestTemplate restTemplate;
    private Long id;

    /**
     * GroupKey——每个Setter必备，默认情况下，会让同一个Setter使用同一个线程池
     * CommandKey——可选参数
     * ThreadPoolKey——不使用默认方式划分线程池，使用显示声明的进行划分
     *
     * @param restTemplate
     * @param id
     */
    public UserCommand(RestTemplate restTemplate, Long id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ribbon-hystrixCommand")).
                andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        // execution 配置
                        // 设置实例隔离的策略为线程池——默认THREAD
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        // 设置执行超时时间——默认1000ms
                        .withExecutionTimeoutInMilliseconds(1000)
                        // 设置是启用超时时间——默认启用，如果为false，则上面的设置 withExecutionTimeoutInMilliseconds 不起作用
                        .withExecutionTimeoutEnabled(true)
                        // 设置执行超时将其打断——默认启用
                        .withExecutionIsolationThreadInterruptOnTimeout(true)
                        // 设置执行被取消时将其打断——默认启用
                        .withExecutionIsolationThreadInterruptOnFutureCancel(true)
                        // 设置隔离措施为 SEMAPHORE 信号量的时候，信号量最大值（并发请求数），后续请求被拒绝——默认10
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)
                        // fallback 配置
                        // 设置从调用线程中允许 HystrixCommand.getFallback() 方法执行的最大次数——默认10，超过后，后续请求会被拒并抛异常
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(10)
                        // 设置服务降级策略启用——默认启用
                        .withFallbackEnabled(true)
                        // circuitBreaker 配置
                        // 服务请求失败后，使用断路器跟踪健康指标和断路请求
                        .withCircuitBreakerEnabled(true)
                        // 设置滚动窗口时间（默认10s）内，熔断器熔断的最小请求数——默认20，即使19个都失败了，熔断器也不会打开
                        .withCircuitBreakerRequestVolumeThreshold(20)
                        // 设置断路器打开之后的休眠时间——默认5s，时间结束后，会将断路器设置为“半开”状态，尝试熔断的请求命令，依然“失败”，熔断器继续设置“打开”状态，反之，关闭熔断器
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
                        // 设置断路器打开错误百分比——默认50%，默认窗口时间内，在请求数量超过阈值 circuitBreakerRequestVolumeThreshold
                        // 阈值的前提下，如果错误请求百分比超过50%，则打开断路器
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        // 关闭断路器强制打开，拒绝所有请求——注意，该设置属性优先于 forceClosed
                        .withCircuitBreakerForceOpen(false)
                        // 关闭断路器强制关闭开关，当 forceOpen 设置为 true 时，该属性不会生效
                        .withCircuitBreakerForceClosed(false)
                        // metrics 配置
                        // 设置滚动时间窗的长度——注意Hystrix 1.4.12版本起，只有应用初始化的时候生效，通过动态刷新配置不生效，避免运行监控数据丢失
                        // 注意，该数量必须能被 metricsRollingStatisticalWindowBuckets 数量整除，不然抛异常
                        .withMetricsRollingStatisticalWindowInMilliseconds(10000)
                        // 设置滚动时间窗统计指标信息划分“buckets”的数量，和 metricsRollingStatisticalWindowInMilliseconds 一样
                        // 只有应用初始化的时候生效，通过动态刷新配置不生效，避免运行监控数据丢失
                        .withMetricsRollingStatisticalWindowBuckets(10)
                        // 设置对命令执行延迟使用百分位数来跟踪和计算——默认true,如果为false，则返回-1
                        .withMetricsRollingPercentileEnabled(true)
                        // 设置百分位统计的滚动时间窗口——默认1min，注意Hystrix 1.4.12版本起，只有应用初始化的时候生效，通过动态刷新配置不生效，避免运行监控数据丢失
                        .withMetricsRollingPercentileWindowInMilliseconds(60000)
                        // 设置统计执行过程 Percentile 中，每个“Bucket” 保留的最大执行次数。如果百分位统计的滚动时间窗口时间内，超过该值
                        // 则保留最后执行100次执行的统计，FIFO。另外增加该值的大小会增加内存量的消耗，并增加排序百分位数所需的计算时间
                        // 注意Hystrix 1.4.12版本起，只有应用初始化的时候生效，通过动态刷新配置不生效，避免运行监控数据丢失
                        .withMetricsRollingPercentileBucketSize(100)
                        // 断路器健康快照采集窗口间隔 500ms
                        .withMetricsHealthSnapshotIntervalInMilliseconds(500)
                        // requestContext 配置
                        // 设置开启请求缓存，默认开启
                        .withRequestCacheEnabled(true)
                        // 设置执行和事件打印到 HystrixRequestLog 中，默认开启
                        .withRequestLogEnabled(true)
                )
                .andCommandKey(GETTER_KEY).andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolKey")).
                        andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().
                                // threadPool 设置
                                // 设置执行命令线程池的核心线程数——默认10
                                withCoreSize(10)
                                //设置线程池的最大队列大小：-1——线程池使用 SynchronousQueue 实现队列，否则使用 LinkedBlockingQueue
                                .withMaxQueueSize(-1)
                                // 为队列设置拒绝阈值。即使队列没有达到最大数，也能拒绝请求。该参数主要是对 LinkedBlockingQueue 队列的补充，因为 LinkedBlockingQueue 不能动态修改它的对象大小
                                // 当MaxQueueSize = -1 时，该参数不起作用
                                .withQueueSizeRejectionThreshold(5)
                                // 设置滚动时间窗的长度——用于线程池指标的衡量，会被分成多个 bucket 来衡量
                                .withMetricsRollingStatisticalWindowInMilliseconds(10000)
                                // 设置滚动时间窗被切分成 bucket 的数量
                                // metricsRollingStatisticalWindowInMilliseconds 必须能被该参数整除
                                .withMetricsRollingStatisticalWindowBuckets(10)
                        ));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected UserVO run() {
        //1、写操作
        UserVO userVO = restTemplate.getForObject("http://HELLO-SERVICE/users/{id}", UserVO.class, id);
        //2、id为2的倍数，刷新缓存
        if (id % 2 == 0) {
            UserCommand.flushCache(id);
        }
        return userVO;
    }

    /**
     * 定义服务降级
     *
     * @return
     */
    @Override
    protected UserVO getFallback() {
        UserVO userVO = new UserVO();
        //negative Integer means failed
        userVO.setId(-1L);
        userVO.setRegistrationTime(new Date());
        return userVO;
    }

    /**
     * 根据Id置入缓存
     * 会在run()和construct()方法之前执行
     *
     * @return
     */
    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    /**
     * 根据id,清除缓存
     * https://github.com/Netflix/Hystrix/wiki/How-To-Use#Caching
     *
     * @param id
     */
    public static void flushCache(Long id) {
        HystrixRequestCache.getInstance(GETTER_KEY, HystrixConcurrencyStrategyDefault.getInstance())
                .clear(String.valueOf(id));
    }
}
