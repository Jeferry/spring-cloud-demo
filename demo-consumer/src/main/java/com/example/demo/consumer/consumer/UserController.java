/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.consumer.service.UserAnnotationService;
import com.example.demo.consumer.service.UserCommand;
import com.example.demo.consumer.service.UserObservableCommand;
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
import java.util.concurrent.atomic.AtomicBoolean;

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
     * 同步调用-发射一次Observable
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
     * 异步调用-发射一次Observable
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
     * HystrixObservableCommand式多次发射Observable
     * hot observe
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/userObservableCommand")
    public List<UserVO> consumerUserObserve(Long id) {
        Observable<UserVO> ho = new UserObservableCommand(restTemplate, id).observe();
        return executeObserve(ho);
    }

    /**
     * HystrixObservableCommand式多次发射Observable
     * cold observable
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/userObserveCommand")
    public List<UserVO> consumerUserObservable(Long id) {
        Observable<UserVO> co = new UserObservableCommand(restTemplate, id).toObservable();
        return executeObserve(co);
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
     * 注解式响应式命令-ho
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/annotation/eager")
    public List<UserVO> getUserObserve(Long id) {
        Observable<UserVO> ho = userAnnotationService.getUserObservableById(id);
        return executeObserve(ho);
    }

    /**
     * hot observable 响应式执行
     * 不论“事件源”是否有“订阅者”，都会在创建后对时间进行发布，所以 hot observable
     * 的每一个“订阅者”都有可能是从事件源的中途开始的，并可能只看到了整个操作的局部过程。
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/observe")
    public List<UserVO> getUserByObserve(Long id) {
        // hot observable-asynchronous
        Observable<UserVO> ho = new UserCommand(restTemplate, id).observe();
        return executeObserve(ho);
    }

    /**
     * cold observable 响应式执行
     * cold observable 在没有“订阅者”的时候并不会发布事件，而是进行等待，直到有“订阅者”后才进行发布，
     * 所以对于 cold observable 的“订阅者”，它可以保证从一开始看到整个操作的全部过程。
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/observable")
    public List<UserVO> getUserByObservable(Long id) {
        // cold observable-asynchronous
        Observable<UserVO> co = new UserCommand(restTemplate, id).toObservable();
        return executeObserve(co);
    }

    /**
     * 观察者-订阅执行
     *
     * @param observable
     * @return
     */
    private List<UserVO> executeObserve(Observable<UserVO> observable) {
        List<UserVO> userVOList = new ArrayList<>(16);
        if (observable != null) {
            AtomicBoolean completeFlag = new AtomicBoolean(false);
            // subscribe
            observable.subscribe(new Observer<UserVO>() {
                @Override
                public void onCompleted() {
                    completeFlag.set(true);
                    System.out.println(JSONObject.toJSONString(userVOList, true));
                }

                @Override
                public void onError(Throwable e) {
                    completeFlag.set(true);
                    logger.error("error happened!", e);
                }

                @Override
                public void onNext(UserVO userVO) {
                    userVOList.add(userVO);
                }
            });

            //wait because execute the subscribe method is asynchronous
            for (; ; ) {
                if (completeFlag.get()) {
                    break;
                }
            }
        }
        return userVOList;
    }


}
