/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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

    /**
     * spring cloud 2.0.0 RELEASE 中，需要自定义/hystrix.stream这个servlet
     * 需要访问一次其他调用的接口，否则该接口一直返回空
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<HystrixMetricsStreamServlet> getHystrixStreamServlet() {
        ServletRegistrationBean<HystrixMetricsStreamServlet> servletBean = new ServletRegistrationBean<>(
                new HystrixMetricsStreamServlet(), "/hystrix.stream"
        );
        servletBean.setLoadOnStartup(1);
        servletBean.setName("HystrixMetricsStreamServlet");
        return servletBean;
    }

}
