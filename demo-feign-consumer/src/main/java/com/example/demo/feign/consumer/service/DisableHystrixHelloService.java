/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author maojifeng
 * @version DisableHystrixHelloService.java, v 0.1 maojifeng
 * @date 2018/3/28 17:56
 * @comment 关闭Hystrix的Feign，需要配置@FeignClient中的configuration
 */
@FeignClient(name = "HELLO-SERVICE"/*, configuration = DisableHystrixConfiguration.class*/)
public interface DisableHystrixHelloService {

    /**
     * 调用 hello-service 服务的 /hello/{id} url
     *
     * @return
     */
    @RequestMapping(value = "/hello/{id}")
    String hello(@PathVariable("id") String id);
}
