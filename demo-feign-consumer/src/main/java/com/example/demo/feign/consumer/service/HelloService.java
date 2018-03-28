/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.service;

import com.example.demo.modules.feign.HelloFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author maojifeng
 * @version HelloService.java, v 0.1 maojifeng
 * @date 2018/3/28 11:55
 * @comment 绑定服务
 */
@FeignClient("hello-service")// 服务名不区分大小写，所以使用 hello-service 和 HELLO-SERVICE 都是可以的。
public interface HelloService {

    /**
     * 调用 hello-service 服务的 /hello url
     *
     * @return
     */
    @RequestMapping(value = "/hello")
    String hello();

    /**
     * 调用 hello-service 服务的 /hello1 url
     * 如果这里不加 @RequestHeader("name") 请求会变成 POST
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/hello1", method = GET)
    String hello(@RequestParam("name") String name);

    /**
     * 调用 hello-service 服务的 /hello2 url
     * 注意@RequestHeader("name")后面的 value 值不能少，否则报错
     *
     * @param name
     * @param age
     * @return
     */
    @RequestMapping(value = "/hello2", method = GET)
    HelloFeign hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age);

    /**
     * 调用 hello-service 服务的 /hello3 url
     *
     * @param helloFeign
     * @return
     */
    @RequestMapping(value = "/hello3", method = POST)
    String hello(@RequestBody HelloFeign helloFeign);


}
