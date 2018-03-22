/**
 * @author maojifeng
 * @date 2018/3/22 14:18
 * @version module-info,v 0.1 maojifeng
 * @comment
 */
module demo.consumer.main {
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.cloud.commons;
    requires spring.context;
    requires spring.web;
    requires hystrix.javanica;
    requires org.apache.commons.lang3;
    requires slf4j.api;
    requires spring.beans;
    requires rxjava;
    requires hystrix.core;
    requires spring.webmvc;
    requires tomcat.embed.core;
    requires ribbon.loadbalancer;
    requires fastjson;
    requires demo.modules.main;
    requires demo.commons.main;
    requires demo.biz.main;
}