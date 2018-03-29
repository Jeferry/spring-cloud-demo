/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.api.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author maojifeng
 * @version AccessFilter.java, v 0.1 maojifeng
 * @date 2018/3/29 18:09
 * @comment 检查过滤器
 */
public class AccessFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 过滤器类型：pre 代表会在请求被路由之前执行
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器执行顺序：当请求在一个阶段中存在多个过滤器时，该值决定执行顺序，小的先执行
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行该过滤器
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器执行方法
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        logger.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());

        Object accessToken = request.getParameter("accessToken");
        if (accessToken == null) {
            logger.warn("access token is empty");
            // 校验失败，不对其路由
            ctx.setSendZuulResponse(false);
            // 返回失败
            ctx.setResponseStatusCode(403);
            return null;
        }
        logger.info("access token ok!");
        return null;
    }
}
