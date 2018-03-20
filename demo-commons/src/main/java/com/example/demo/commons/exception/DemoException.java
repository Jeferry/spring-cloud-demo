/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.commons.exception;

/**
 * @author maojifeng
 * @version DemoException.java, v 0.1 maojifeng
 * @date 2018/3/20 16:20
 * @comment 异常封装
 */
public class DemoException extends RuntimeException {

    private static final long serialVersionUID = -5027901203294720439L;

    public DemoException() {
        super();
    }

    public DemoException(String message) {
        super(message);
    }

    public DemoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DemoException(Throwable cause) {
        super(cause);
    }

    protected DemoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
