package com.example.demo.web;

import com.example.demo.web.index.WelcomeController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootApplication(scanBasePackageClasses = DemoApplicationTests.class)//指定springBoot启动类
@SpringBootTest
//@WebAppConfiguration//开启web应用的配置，用于模拟ServletContext
public class DemoApplicationTests {

    private MockMvc mvc;

    @Before
    public void setMvc() {
        mvc = MockMvcBuilders.standaloneSetup(new WelcomeController()).build();
    }

    @Test
    public void helloTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(equalTo("hello world")));
    }

}