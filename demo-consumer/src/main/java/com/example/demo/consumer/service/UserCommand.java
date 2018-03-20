/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.service;

import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @author maojifeng
 * @version UserCommand.java, v 0.1 maojifeng
 * @date 2018/3/19 11:13
 * @comment 用户请求封装-只能发射一次
 */
public class UserCommand extends HystrixCommand<UserVO> {

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
                .andCommandKey(HystrixCommandKey.Factory.asKey("userCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolKey")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected UserVO run() {
        return restTemplate.getForObject("http://HELLO-SERVICE/users/{id}", UserVO.class, id);
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
}
