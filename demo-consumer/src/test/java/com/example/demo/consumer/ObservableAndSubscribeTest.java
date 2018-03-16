/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Observable;
import rx.Subscriber;

/**
 * @author maojifeng
 * @version ObservableAndSubscribeTest.java, v 0.1 maojifeng
 * @date 2018/3/16 15:18
 * @comment 订阅者，观察者模式
 */
@RunWith(SpringRunner.class)
public class ObservableAndSubscribeTest {

    @Test
    public void observableTest() {
        //1、创建事件源 observable
        Observable<String> observable = Observable.create(subscriber -> {
            subscriber.onNext("hello RxJava");
            subscriber.onNext("I'm JeFerry");
            subscriber.onCompleted();
        });

        //2、创建订阅者 subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("Subscribe : " + s);
            }
        };

        //3、订阅
        observable.subscribe(subscriber);
    }

}
