/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.api.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maojifeng
 * @version IndexController.java, v 0.1 maojifeng
 * @date 2018/3/30 15:27
 * @comment 本地控制层
 */
@RestController
@RequestMapping(value = "/local")
public class IndexController {

    /**
     * 本地跳转请求
     *
     * @return
     */
    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello World from local";
    }


}
