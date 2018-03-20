/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.service;

import com.example.demo.modules.UserVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.Future;

/**
 * @author maojifeng
 * @version UserAnnotationService.java, v 0.1 maojifeng
 * @date 2018/3/19 15:41
 * @comment 通过@HystrixAnnotation注解的实现
 */
@Service
public class UserAnnotationService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 同步方法
     *
     * @param id
     * @return
     */
    @HystrixCommand
    public UserVO getUserByIdSync(Long id) {
        return wrapperRequest(id);
    }

    /**
     * 异步方法
     *
     * @param id
     * @return
     */
    @HystrixCommand
    public Future<UserVO> getUserByIdAsync(final Long id) {
        return new AsyncResult<UserVO>() {
            @Override
            public UserVO invoke() {
                return wrapperRequest(id);
            }
        };
    }

    /**
     * 注解式响应式命令
     * EAGER表示使用toObserve()执行方式 hot observable
     * Lazy表示使用toObservable()执行方式 cold observable
     *
     * @param id
     * @return
     */
    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER)
//    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.LAZY)
    public Observable<UserVO> getUserObservableById(final Long id) {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    UserVO userVO = wrapperRequest(id);
                    subscriber.onNext(userVO);
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private UserVO wrapperRequest(final Long id) {
        return restTemplate.getForObject("http://HELLO-SERVICE/users/{id}", UserVO.class, id);
    }

}
