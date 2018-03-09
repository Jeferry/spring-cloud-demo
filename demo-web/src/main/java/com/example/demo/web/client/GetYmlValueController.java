package com.example.demo.web.client;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.biz.GetYmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author maojifeng
 * @version GetYmlValueController.java, v 0.1 maojifeng
 * @date 2018/3/7 14:33
 * @comment 获取配置文件controller
 */
@RestController
@RequestMapping(value = "/yml/", produces = APPLICATION_JSON_VALUE)
public class GetYmlValueController {

    @Autowired
    private GetYmlService getYmlService;

    @RequestMapping(value = "/bookDesc")
    public String getBookDesc() {
        return getYmlService.getBookDesc();
    }

    @RequestMapping(value = "/random")
    public JSONObject getRandom() {
        return getYmlService.getRandom();
    }

}
