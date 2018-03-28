/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.service;

import com.example.demo.biz.BaseHelloService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author maojifeng
 * @version RefactorHelloService.java, v 0.1 maojifeng
 * @date 2018/3/28 16:03
 * @comment 继承自基类BaseHelloService的服务类
 */
@FeignClient(value = "HELLO-SERVICE")
public interface RefactorHelloService extends BaseHelloService {

}
