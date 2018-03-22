/**
 * @author maojifeng
 * @date 2018/3/22 11:55
 * @version module-info,v 0.1 maojifeng
 * @comment
 */
module demo.web.main {
    requires spring.web;
    requires spring.context;
    requires spring.boot.actuator;
    requires spring.beans;
    requires slf4j.api;
    requires spring.cloud.commons;
    requires commons.collections;
    requires ribbon.loadbalancer;
    requires spring.cloud.netflix.ribbon;
    requires demo.commons.main;
    requires spring.boot;
    requires demo.biz.main;
    requires demo.modules.main;
    requires fastjson;
}