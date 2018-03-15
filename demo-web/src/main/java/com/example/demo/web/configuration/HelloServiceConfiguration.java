/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.web.configuration;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.context.annotation.Bean;

/**
 * @author maojifeng
 * @version HelloServiceConfiguration.java, v 0.1 maojifeng
 * @date 2018/3/15 11:23
 * @comment hello-service配置文件
 */
public class HelloServiceConfiguration {

    @Bean
    public IPing ribbonConfig() {
        return new PingUrl();
    }

}
