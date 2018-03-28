/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.web.client;

import com.example.demo.modules.feign.HelloFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author maojifeng
 * @version HelloFeignController.java, v 0.1 maojifeng
 * @date 2018/3/28 14:28
 * @comment Feign调用的控制层
 */
@RestController
public class HelloFeignController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/hello1", method = GET)
    public String hello(String name) {
        return "hello" + name;
    }

    @RequestMapping(value = "/hello2", method = GET)
    public HelloFeign hello(@RequestHeader String name, @RequestHeader Integer age) {
        return new HelloFeign(name, age);
    }

    @RequestMapping(value = "/hello3", method = POST)
    public String hello(@RequestBody HelloFeign helloFeign) {
        return "Hello" + helloFeign.toString();
    }

}
