/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.web.client;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author maojifeng
 * @version HelloController.java, v 0.1 maojifeng
 * @date 2018/3/8 17:08
 * @comment Eureka服务注册
 */
@RestController
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/hello", method = GET)
    public String eurekaHello() throws InterruptedException {
        List<String> service = client.getServices();
        logger.info("all service below:" + JSONObject.toJSONString(service));
        // eureka 现在通过serviceId获取服务，serviceId即注册服务配置文件中的${eureka.client.name}
        if (CollectionUtils.isNotEmpty(service)) {
            service.forEach(serviceId -> {
                //获取各个serviceId的所有服务
                printListServiceInstance(client.getInstances(serviceId));
            });
        }
        // hystrix 默认超时时间为20000ms,这里采用随机数来模拟
        int randomSleepTime = new Random().nextInt(3000);
        logger.info("sleepTime:{}", randomSleepTime);
        Thread.sleep(randomSleepTime);
        return "hello world!";

    }

    private void printListServiceInstance(List<ServiceInstance> serviceInstances) {
        if (CollectionUtils.isNotEmpty(serviceInstances)) {
            serviceInstances.forEach(serviceInstance -> logger.info("serviceInstances:" + JSONObject.toJSONString(serviceInstances, true)));
        }
    }
}
