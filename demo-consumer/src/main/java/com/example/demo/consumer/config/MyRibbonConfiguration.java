/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.config;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建了PingUrl实例，默认的NoOpPing就不会创建
 *
 * @author maojifeng
 * @version MyRibbonConfiguration.java, v 0.1 maojifeng
 * @date 2018/3/15 11:10
 * @comment 自定义Ribbon配置
 */
@Configuration
public class MyRibbonConfiguration {

    @Bean
    public IPing ribbonPing() {
        return new PingUrl();
    }

}
