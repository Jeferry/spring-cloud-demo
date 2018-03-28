/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.controller;

import com.example.demo.feign.consumer.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/feign-consumer", method = GET)
    public String helloConsumer() {
        return helloService.hello();
    }


}
