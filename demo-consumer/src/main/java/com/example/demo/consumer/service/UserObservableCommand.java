/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.service;

import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

/**
 * @author maojifeng
 * @version UserObservableCommand.java, v 0.1 maojifeng
 * @date 2018/3/20 13:46
 * @comment 用户请求封装-能发射多次
 */
public class UserObservableCommand extends HystrixObservableCommand<UserVO> {

    private RestTemplate restTemplate;
    private Long id;

    public UserObservableCommand(RestTemplate restTemplate, Long id) {
        super(HystrixObservableCommand.Setter.withGroupKey(asKey("ribbon-hystrixObservableCommand")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected Observable<UserVO> construct() {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    UserVO userVO = restTemplate.getForObject("http://HELLO-SERVICE/users/{id}", UserVO.class, id);
                    subscriber.onNext(userVO);
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
