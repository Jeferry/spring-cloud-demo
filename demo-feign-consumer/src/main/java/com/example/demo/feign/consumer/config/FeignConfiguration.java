/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.feign.consumer.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maojifeng
 * @version FeignConfiguration.java, v 0.1 maojifeng
 * @date 2018/3/28 17:52
 * @comment 关闭Hystrix的配置类，现在feign默认是关闭状态——重写 builder  会导致 fallback 不起作用
 */
@Configuration
public class FeignConfiguration {

//    @Bean
//    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//    public Feign.Builder feignBuilder() {
//        return Feign.builder();
//    }

    /**
     * 默认 log 级别为：NONE，配置 log 必须打开此设置
     *
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
