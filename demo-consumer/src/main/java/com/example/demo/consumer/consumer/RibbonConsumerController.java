/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.consumer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author maojifeng
 * @version RibbonConsumerController.java, v 0.1 maojifeng
 * @date 2018/3/9 17:59
 * @comment 消费者接口
 */
@RestController
public class RibbonConsumerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 消费调用
     *
     * @return
     */
    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "helloFallback", commandKey = "helloKey")
    public String helloConsumer() {
        long start = System.currentTimeMillis();
        String result = restTemplate.getForObject("http://HELLO-SERVICE/hello", String.class);
        long end = System.currentTimeMillis();
        logger.info("Spend time : {}", (end - start));
        return result;
    }

    public String helloFallback() {
        return "hello fallback";
    }


}
