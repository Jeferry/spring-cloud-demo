/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.web.client;

import com.example.demo.biz.BaseHelloService;
import com.example.demo.modules.feign.HelloFeign;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maojifeng
 * @version RefactorHelloController.java, v 0.1 maojifeng
 * @date 2018/3/28 15:59
 * @comment 实现基类BaseHelloService的Controller
 */
@RestController
public class RefactorHelloController implements BaseHelloService {

    @Override
    public String hello(@RequestParam("name") String name) {
        return "hello:" + name;
    }

    @Override
    public HelloFeign hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age) {
        return new HelloFeign(name, age);
    }

    @Override
    public String hello(@RequestBody HelloFeign helloFeign) {
        return helloFeign.toString();
    }
}
