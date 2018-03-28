/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author maojifeng
 * @version HelloService.java, v 0.1 maojifeng
 * @date 2018/3/28 11:55
 * @comment 绑定服务
 */
@FeignClient("hello-service")// 服务名不区分大小写，所以使用 hello-service 和 HELLO-SERVICE 都是可以的。
public interface HelloService {

    @RequestMapping(value = "/hello")
    String hello();

}
