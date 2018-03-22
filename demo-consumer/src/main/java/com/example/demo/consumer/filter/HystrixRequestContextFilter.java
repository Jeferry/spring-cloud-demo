/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.consumer.filter;

import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Collection;

/**
 * @author maojifeng
 * @version HystrixRequestContextFilter.java, v 0.1 maojifeng
 * @date 2018/3/22 18:00
 * @comment Hystrix请求上下文初始化过滤器
 */
@Component
@WebFilter(urlPatterns = "/ribbon-consumer/*")
public class HystrixRequestContextFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            chain.doFilter(request, response);
        } finally {
            getHystrixRequestLog();
            context.shutdown();
        }

    }

    @Override
    public void destroy() {

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
