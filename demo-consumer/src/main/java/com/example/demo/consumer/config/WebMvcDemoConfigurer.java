/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.config;

import com.example.demo.consumer.interceptor.HystrixInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author maojifeng
 * @version WebMvcDemoConfigurer.java, v 0.1 maojifeng
 * @date 2018/3/21 13:48
 * @comment 添加拦截器监听器等配置
 */
@Configuration
public class WebMvcDemoConfigurer implements WebMvcConfigurer {

    @Autowired
    private HystrixInterceptor hystrixInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(hystrixInterceptor).addPathPatterns("/ribbon-consumer/**");
    }
}
