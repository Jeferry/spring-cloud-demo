/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.interceptor;

import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author maojifeng
 * @version HystrixInterceptor.java, v 0.1 maojifeng
 * @date 2018/3/21 13:53
 * @comment HystrixRequestContext initializing through this interceptor
 */
@Component
public class HystrixInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ThreadLocal<HystrixRequestContext> context = ThreadLocal.withInitial(HystrixRequestContext::initializeContext);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // HystrixRequestContext initializing
        context.set(HystrixRequestContext.initializeContext());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // get request log
        getHystrixRequestLog();
        HystrixRequestContext hystrixRequestContext = context.get();
        if (hystrixRequestContext != null) {
            hystrixRequestContext.shutdown();
        }
        context.remove();
    }

    private void getHystrixRequestLog() {
        // assert that the batch command 'UserBatchCommand' was in fact
        // executed and that it executed only once
        logger.info("all executedCommand:{}", HystrixRequestLog.getCurrentRequest().getExecutedCommandsAsString());
        logger.info("executed times: {}", HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().size());
        Collection<HystrixInvokableInfo<?>> invokableIfs = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands();
        invokableIfs.forEach(invokableInfo -> {
            // assert the command is the one we're expecting
            logger.info("commandKey:{}", invokableInfo.getCommandKey().name());
            // confirm that it was a COLLAPSED command execution
            // and that it was successful
            logger.info("ExecutionEvents:{}", invokableInfo.getExecutionEvents());
        });

    }
}
