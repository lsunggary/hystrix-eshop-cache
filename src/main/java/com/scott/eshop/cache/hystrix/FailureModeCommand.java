package com.scott.eshop.cache.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * @ClassName FailureModeCommand
 * @Description
 * @Author 47980
 * @Date 2020/5/9 22:01
 * @Version V_1.0
 **/
public class FailureModeCommand extends HystrixCommand<Boolean> {

    private Boolean failure;

    public FailureModeCommand(Boolean failure) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FailureModeGroup")));
        this.failure = failure;
    }
    @Override
    protected Boolean run() throws Exception {
        if (failure) {
            throw new Exception();
        }
        return failure;
    }

    @Override
    protected Boolean getFallback() {
        return false;
    }
}
