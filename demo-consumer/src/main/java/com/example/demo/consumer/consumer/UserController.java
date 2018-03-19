/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.consumer.service.UserAnnotationService;
import com.example.demo.consumer.service.UserCommand;
import com.example.demo.modules.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.List;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

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

    /**
     * hot observable 响应式执行
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/observe")
    public List<UserVO> getUserByObserve(Long id) {
        // hot observable-asynchronous
        Observable<UserVO> ho = new UserCommand(restTemplate, id).observe();

        List<UserVO> userVOList = new ArrayList<>(16);
        // subscribe
        ho.subscribe(new Observer<UserVO>() {
            @Override
            public void onCompleted() {
                System.out.println(JSONObject.toJSONString(userVOList, true));
            }

            @Override
            public void onError(Throwable e) {
                logger.error("error happened!", e);
            }

            @Override
            public void onNext(UserVO userVO) {
                userVOList.add(userVO);
            }
        });

        //wait because execute the subscribe method is asynchronous
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            logger.error("InterruptedException!", e);
        }

        return userVOList;

    }

    /**
     * cold observable 响应式执行
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/observable")
    public List<UserVO> getUserByObservable(Long id) {
        // cold observable-asynchronous
        Observable<UserVO> co = new UserCommand(restTemplate, id).toObservable();
        List<UserVO> userVOList = new ArrayList<>(16);
        // subscribe
        co.subscribe(new Observer<UserVO>() {
            @Override
            public void onCompleted() {
                System.out.println(JSONObject.toJSONString(userVOList, true));
            }

            @Override
            public void onError(Throwable e) {
                logger.error("error happened!", e);
            }

            @Override
            public void onNext(UserVO userVO) {
                userVOList.add(userVO);
            }
        });

        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            logger.error("InterruptedException!", e);
        }

        return userVOList;
    }


}
