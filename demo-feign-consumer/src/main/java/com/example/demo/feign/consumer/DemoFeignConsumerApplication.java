package com.example.demo.feign.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author maojifeng
 */
@EnableFeignClients //开启 Spring Cloud Feign 支持
@EnableCircuitBreaker   // 开启断路器功能 ，否则会导致 @FeignClient 中配置的 fallback 服务降级，不起作用
@EnableDiscoveryClient
@SpringBootApplication
public class DemoFeignConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoFeignConsumerApplication.class, args);
    }
}
