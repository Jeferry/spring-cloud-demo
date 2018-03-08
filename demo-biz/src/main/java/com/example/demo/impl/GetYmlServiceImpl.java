package com.example.demo.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.GetYmlService;
import com.example.demo.HelloVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author mao
 */
@Service
public class GetYmlServiceImpl implements GetYmlService {

    /**
     * 自定义属性(可以通过上下文的方式引用)
     */
    @Value("${book.desc}")
    private String bookDesc;

    /**
     * 随机字符 - 只在加载的时候随机产生，后面就固定了
     */
    @Value("${com.jeferry.value}")
    private String value;
    /**
     * 随机int
     */
    @Value("${com.jeferry.number}")
    private String number;
    /**
     * 随机long
     */
    @Value("${com.jeferry.bigNumber}")
    private String bigNumber;
    /**
     * 10以内随机int
     */
    @Value("${com.jeferry.tinyInt}")
    private String tinyInt;
    /**
     * 10-20随机数
     */
    @Value("${com.jeferry.randomValue}")
    private String randomValue;

    @Override
    public String getBookDesc() {
        return bookDesc;
    }

    @Override
    public JSONObject getRandom() {
        JSONObject js = new JSONObject(8);
        js.put("value", value);
        js.put("number", number);
        js.put("bigNumber", bigNumber);
        js.put("tinyInt", tinyInt);
        js.put("randomValue", randomValue);
        HelloVO helloVO = new HelloVO();
        helloVO.setName("mao");
        helloVO.setAge(1);
        js.put("helloVO", helloVO);
        return js;
    }
}
