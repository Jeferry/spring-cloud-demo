/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.service;

import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.web.client.RestTemplate;

/**
 * @author maojifeng
 * @version UserCommand.java, v 0.1 maojifeng
 * @date 2018/3/19 11:13
 * @comment 用户请求封装
 */
public class UserCommand extends HystrixCommand<UserVO> {

    private RestTemplate restTemplate;
    private Long id;

    public UserCommand(RestTemplate restTemplate, Long id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ribbon")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected UserVO run() {
        return restTemplate.getForObject("http://HELLO-SERVICE/users/{id}", UserVO.class, id);
    }
}
