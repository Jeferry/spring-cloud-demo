/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.service.impl;

import com.example.demo.feign.consumer.service.HelloService;
import com.example.demo.modules.feign.HelloFeign;
import org.springframework.stereotype.Component;

/**
 * @author maojifeng
 * @version HelloServiceFallBackImpl.java, v 0.1 maojifeng
 * @date 2018/3/29 11:17
 * @comment helloService 服务降级逻辑实现
 */
@Component
public class HelloServiceFallBackImpl implements HelloService {

    /**
     * 失败提示
     */
    private static final String ERROR = "error";

    @Override
    public String hello() {
        return ERROR;
    }

    @Override
    public String hello(String name) {
        return ERROR;
    }

    @Override
    public HelloFeign hello(String name, Integer age) {
        return new HelloFeign("unknown", 0);
    }

    @Override
    public String hello(HelloFeign helloFeign) {
        return ERROR;
    }
}
