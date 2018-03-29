package com.example.demo.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author maojifeng
 */
@EnableZuulProxy // 开启 ZUUL API 的网关功能
@SpringBootApplication
public class DemoApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApiGatewayApplication.class, args);
    }
}
