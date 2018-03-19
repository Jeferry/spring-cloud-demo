/**
 * YOUTU SOFTWARE Inc.
 * Copyright (c) 2016-2018 All Rights Reserved.
 */
package com.example.demo.web.client;

import com.example.demo.modules.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author maojifeng
 * @version UserController.java, v 0.1 maojifeng
 * @date 2018/3/19 11:08
 * @comment 用户控制层
 */
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/users/{id}")
    public UserVO getUserById(@PathVariable Long id) {
        logger.info("hello users:id={}", id);
        UserVO userVO = new UserVO();
        userVO.setId(id);
        userVO.setRegistrationTime(new Date());
        return userVO;
    }

}
