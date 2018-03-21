/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.service;

import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.HystrixThreadPoolKey;
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
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ribbon-hystrixCommand"))
                .andCommandKey(GETTER_KEY).andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolKey")));
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
