package com.example.demo.feign.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author maojifeng
 */
@EnableFeignClients //开启 Spring Cloud Feign 支持
@EnableDiscoveryClient
@SpringBootApplication
public class DemoFeignConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoFeignConsumerApplication.class, args);
    }
}
