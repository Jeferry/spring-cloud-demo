/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.service;

import com.example.demo.feign.consumer.service.impl.HelloServiceFallBackImpl;
import com.example.demo.modules.feign.HelloFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * fallback 指定服务降级方法生效必要条件：
 * 1、由于当前版本 feign 是关闭 hystrix 的支持，要开启，首先应引入依赖 compile('org.springframework.cloud:spring-cloud-starter-netflix-hystrix')
 * 2、打开 hystrix 的支持，配置文件中设置：feign.hystrix.enable=true 还有为什么Feign对Hystrix的支持默认是关闭状态的原因讨论：https://github.com/spring-cloud/spring-cloud-netflix/issues/1277
 * 3、启动类加上开启断路器功能注解 @EnableCircuitBreaker
 * 4、配置 fallback 方法，里面的类必须是接口的实现类，且被标识成组件 @Component
 * 5、Feign.Builder 没有被重写
 *
 * @author maojifeng
 * @version HelloService.java, v 0.1 maojifeng
 * @date 2018/3/28 11:55
 * @comment 绑定服务
 */
// 服务名不区分大小写，所以使用 hello-service 和 HELLO-SERVICE 都是可以的。
@FeignClient(name = "hello-service", fallback = HelloServiceFallBackImpl.class/*, configuration = FeignConfiguration.class*/)
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
