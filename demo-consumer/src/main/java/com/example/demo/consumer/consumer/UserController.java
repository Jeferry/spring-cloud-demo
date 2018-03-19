/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.consumer;

import com.example.demo.consumer.service.UserAnnotationService;
import com.example.demo.consumer.service.UserCommand;
import com.example.demo.modules.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author maojifeng
 * @version UserController.java, v 0.1 maojifeng
 * @date 2018/3/19 11:19
 * @comment 用户控制层
 */
@RestController
@RequestMapping(value = "/ribbon-consumer", produces = APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserAnnotationService userAnnotationService;

    /**
     * 同步调用
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/sync")
    public UserVO consumerUserSync(Long id) {
        UserCommand userCommand = new UserCommand(restTemplate, id);
        return userCommand.execute();
    }

    /**
     * 异步调用
     *
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/async")
    public UserVO consumerUserAsync(Long id) throws ExecutionException, InterruptedException {
        UserCommand userCommand = new UserCommand(restTemplate, id);
        return userCommand.queue().get();
    }

    /**
     * 注解式的同步调用
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/annotation/sync")
    public UserVO getUserSync(Long id) {
        return userAnnotationService.getUserByIdSync(id);
    }


    /**
     * 注解式的异步调用
     *
     * @param id
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/annotation/async")
    public UserVO getUserAsync(Long id) throws ExecutionException, InterruptedException {
        return userAnnotationService.getUserByIdAsync(id).get();
    }


}
