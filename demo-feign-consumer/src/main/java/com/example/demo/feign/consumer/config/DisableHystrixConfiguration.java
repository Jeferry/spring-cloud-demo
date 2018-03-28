/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.config;

import feign.Feign;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author maojifeng
 * @version DisableHystrixConfiguration.java, v 0.1 maojifeng
 * @date 2018/3/28 17:52
 * @comment 关闭Hystrix的配置类
 */
@Configuration
public class DisableHystrixConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

}
