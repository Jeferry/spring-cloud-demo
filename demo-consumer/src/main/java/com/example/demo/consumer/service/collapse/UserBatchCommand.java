package com.example.demo.consumer.service.collapse;

import com.example.demo.consumer.service.UserAnnotationService;
import com.example.demo.modules.UserVO;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import java.util.List;

/**
 * @author mao
 */
public class UserBatchCommand extends HystrixCommand<List<UserVO>> {

    private UserAnnotationService userAnnotationService;
    private List<Long> ids;

    public UserBatchCommand(UserAnnotationService userAnnotationService, List<Long> ids) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ribbon-hystrixCommand")));
        this.userAnnotationService = userAnnotationService;
        this.ids = ids;
    }

    @Override
    protected List<UserVO> run() {
        return userAnnotationService.findAll(ids);
    }
}
