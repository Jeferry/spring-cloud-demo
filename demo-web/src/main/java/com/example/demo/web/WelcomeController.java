package com.example.demo.web;

import com.example.demo.DateTimeUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mao
 */
@RestController
public class WelcomeController {

    @RequestMapping(value = "/")
    public String welcomeList() {
        return "hello world:" + DateTimeUtil.getNow();
    }
}
