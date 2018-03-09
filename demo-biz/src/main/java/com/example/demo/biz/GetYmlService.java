package com.example.demo.biz;

import com.alibaba.fastjson.JSONObject;

/**
 * @author mao
 */
public interface GetYmlService {

    /**
     * 获取配置文件参数
     *
     * @return
     */
    String getBookDesc();

    /**
     * 获取配置文件的随机数
     *
     * @return
     */
    JSONObject getRandom();
}
