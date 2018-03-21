/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.interceptor;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author maojifeng
 * @version HystrixInterceptor.java, v 0.1 maojifeng
 * @date 2018/3/21 13:53
 * @comment HystrixRequestContext initializing through this interceptor
 */
@Component
public class HystrixInterceptor extends HandlerInterceptorAdapter {

    private ThreadLocal<HystrixRequestContext> context = ThreadLocal.withInitial(HystrixRequestContext::initializeContext);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // HystrixRequestContext 上下文初始化
        context.set(HystrixRequestContext.initializeContext());
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HystrixRequestContext hystrixRequestContext = context.get();
        if (hystrixRequestContext != null) {
            hystrixRequestContext.shutdown();
        }
        context.remove();
    }
}
