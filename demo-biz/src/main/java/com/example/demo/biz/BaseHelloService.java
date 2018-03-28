/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.biz;

import com.example.demo.modules.feign.HelloFeign;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author maojifeng
 * @version BaseHelloService.java, v 0.1 maojifeng
 * @date 2018/3/28 15:46
 * @comment HelloService 基类。
 * 这么做虽然可以很方便地实现接口定义和依赖的共享
 * 不用复制粘贴接口进行绑定，但是这样的做法使用不当的话会带来副作用，由于接口在构建
 * 的期间就建立起了依赖关系，那么接口的变动就会对项目构建造成影响，可能服务提供方修改
 * 了一个接口的定义，那么会直接导致客户端工程的构建失败。所以，如果开发团队通过此方法
 * 来实现接口共享的话，建议在开发评审期间，严格遵守面向对象的开闭原则，尽可能地做好前后
 * 版本的兼容，防止牵一发而动全身的后果，增加团队不必要的维护工作量。
 */
@RequestMapping(value = "/refactor")
public interface BaseHelloService {

    /**
     * helloService（web） 和 ConsumerController(feign) 模块共用的接口
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/hello4", method = GET)
    String hello(@RequestParam("name") String name);

    @RequestMapping(value = "/hello5", method = GET)
    HelloFeign hello(@RequestHeader("name") String name, @RequestHeader("age") Integer age);

    @RequestMapping(value = "/hello6", method = POST)
    String hello(@RequestBody HelloFeign helloFeign);
}
