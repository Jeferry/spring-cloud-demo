/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.api.gateway.configuration;

import com.example.demo.api.gateway.filter.AccessFilter;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maojifeng
 * @version ApiGatewayConfiguration.java, v 0.1 maojifeng
 * @date 2018/3/29 18:24
 * @comment API配置类
 */
@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    /**
     * 自定义路由规则
     * 把 /helloservice-v1/** 的请求重新映射成 /helloservice/v1/**
     *
     * @return
     */
    @Bean
    public PatternServiceRouteMapper serviceRouteMapper() {
        return new PatternServiceRouteMapper("(?<name>.*)-(?<version>v.*$)",
                "${version}/${name}");
    }

}
