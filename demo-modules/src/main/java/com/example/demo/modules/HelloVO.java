package com.example.demo.modules;

import java.io.Serializable;

/**
 * @author mao
 */
public class HelloVO implements Serializable {
    private static final long serialVersionUID = 5425449300342896173L;

    private String name;

    private Integer age;

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
}
