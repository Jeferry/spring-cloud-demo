/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.consumer;

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
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 同步调用
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/ribbon-consumer/sync")
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
    @RequestMapping(value = "/ribbon-consumer/async")
    public UserVO consumerUserAsync(Long id) throws ExecutionException, InterruptedException {
        UserCommand userCommand = new UserCommand(restTemplate, id);
        return userCommand.queue().get();
    }


}
