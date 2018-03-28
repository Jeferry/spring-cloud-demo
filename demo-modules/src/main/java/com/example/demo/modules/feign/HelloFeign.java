/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.modules.feign;

import java.io.Serializable;

/**
 * @author maojifeng
 * @version HelloFeign.java, v 0.1 maojifeng
 * @date 2018/3/28 14:24
 * @comment Feign 使用的模型
 */
public class HelloFeign implements Serializable {

    private static final long serialVersionUID = -5969321738309745168L;

    private String name;

    private Integer age;

    public HelloFeign() {
    }

    public HelloFeign(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "HelloFeign{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
