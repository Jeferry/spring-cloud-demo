/**
 * @author maojifeng
 * @date 2018/3/22 11:59
 * @version module-info,v 0.1 maojifeng
 * @comment
 */
module demo.biz.main {
    requires fastjson;
    requires spring.context;
    requires spring.beans;
    requires demo.modules.main;

    exports com.example.demo.biz;
}