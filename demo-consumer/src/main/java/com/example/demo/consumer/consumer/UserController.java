/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.consumer.service.UserAnnotationService;
import com.example.demo.consumer.service.UserCommand;
import com.example.demo.consumer.service.UserObservableCommand;
import com.example.demo.consumer.service.collapse.UserCollapseCommand;
import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.HystrixRequestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
    public List<UserVO> consumerUserSync(Long id) {
        List<UserVO> userVOS = new ArrayList<>(8);

        UserCommand userCommand1 = new UserCommand(restTemplate, id);
        UserCommand userCommand2 = new UserCommand(restTemplate, id + 1);
        UserCommand userCommand3 = new UserCommand(restTemplate, id + 2);
        UserCommand userCommand4 = new UserCommand(restTemplate, id + 1);
        UserCommand userCommand5 = new UserCommand(restTemplate, id);
        userVOS.add(userCommand1.execute());
        userVOS.add(userCommand2.execute());
        userVOS.add(userCommand3.execute());
        userVOS.add(userCommand4.execute());
        userVOS.add(userCommand5.execute());

        return userVOS;
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
    public List<UserVO> getUserSync(Long id) {
        List<UserVO> result = new ArrayList<>(16);
        result.add(userAnnotationService.getUserByIdSync(id));
        result.add(userAnnotationService.getUserByIdSync(id + 1));
        result.add(userAnnotationService.getUserByIdSync(id + 2));
        result.add(userAnnotationService.getUserByIdSync(id + 1));
        result.add(userAnnotationService.getUserByIdSync(id));
        return result;
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
    public List<UserVO> getUserAsync(Long id) throws ExecutionException, InterruptedException {
        List<UserVO> result = new ArrayList<>(16);
        result.add(userAnnotationService.getUserByIdAsync(id).get());
        result.add(userAnnotationService.getUserByIdAsync(id + 1).get());
        result.add(userAnnotationService.getUserByIdAsync(id + 2).get());
        result.add(userAnnotationService.getUserByIdAsync(id + 1).get());
        // remove cache key
        userAnnotationService.removeCache(id);
        result.add(userAnnotationService.getUserByIdAsync(id).get());
        return result;
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
     * Collapse方式查询UserVO,以节省线程池开销
     *
     * @param ids
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/collapse")
    public List<UserVO> getUserCollapse(@RequestParam("ids") List<Long> ids) throws ExecutionException, InterruptedException {
        List<Future<UserVO>> futureList = ids.stream().map(id ->
                new UserCollapseCommand(userAnnotationService, id).queue()).collect(Collectors.toList());
        List<UserVO> result = new ArrayList<>((int) (ids.size() / 0.75 + 1));
        for (Future<UserVO> userVOFuture : futureList) {
            result.add(userVOFuture.get());
        }
        // assert that the batch command 'UserBatchCommand' was in fact
        // executed and that it executed only once
        assert HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().size() == 1;
        logger.info("all executedCommand:{}", HystrixRequestLog.getCurrentRequest().getExecutedCommandsAsString());
        HystrixInvokableInfo<?> invokableInfo = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().toArray(new HystrixInvokableInfo<?>[1])[0];
        // assert the command is the one we're expecting
        assert invokableInfo.getCommandKey().name().equals("UserBatchCommand");
        // confirm that it was a COLLAPSED command execution
        assert invokableInfo.getExecutionEvents().contains(HystrixEventType.COLLAPSED);
        // and that it was successful
        assert invokableInfo.getExecutionEvents().contains(HystrixEventType.SUCCESS);
        return result;
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
