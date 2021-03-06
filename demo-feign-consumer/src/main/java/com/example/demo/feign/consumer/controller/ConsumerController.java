/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.feign.consumer.service.DisableHystrixHelloService;
import com.example.demo.feign.consumer.service.HelloService;
import com.example.demo.feign.consumer.service.RefactorHelloService;
import com.example.demo.modules.feign.HelloFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author maojifeng
 * @version ConsumerController.java, v 0.1 maojifeng
 * @date 2018/3/28 13:55
 * @comment 消费controller
 */
@RestController
public class ConsumerController {

    @Autowired
    private HelloService helloService;
    @Autowired
    private RefactorHelloService refactorHelloService;
    @Autowired
    private DisableHystrixHelloService disableHystrixHelloService;

    @RequestMapping(value = "/feign-consumer", method = GET)
    public String helloConsumer() {
        return helloService.hello();
    }

    @RequestMapping(value = "/feign-consumer2", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public JSONObject helloConsumer2() {
        JSONObject result = new JSONObject(16);
        result.put("hello", helloService.hello());
        result.put("hello1", helloService.hello("Jeferry"));
        result.put("hello2", helloService.hello("Jeferry", 18));
        result.put("hello3", helloService.hello(new HelloFeign("Jeferry", 18)));
        return result;
    }

    @RequestMapping(value = "/feign-consumer3", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public JSONObject helloConsumer3() {
        JSONObject result = new JSONObject(16);
        result.put("hello4", refactorHelloService.hello("Jeferry"));
        result.put("hello5", refactorHelloService.hello("Jeferry", 18));
        result.put("hello6", refactorHelloService.hello(new HelloFeign("Jeferry", 18)));
        return result;
    }

    @RequestMapping(value = "/feign-consumer3/{id}", method = GET)
    public String helloConsumer4(@PathVariable("id") String id) {
        return disableHystrixHelloService.hello(id);
    }


}
