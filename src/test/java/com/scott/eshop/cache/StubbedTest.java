package com.scott.eshop.cache;

import com.scott.eshop.cache.hystrix.GetProductInfoCommand;

/**
 * @ClassName StubbedTest
 * @Description
 * @Author 47980
 * @Date 2020/5/9 22:25
 * @Version V_1.0
 **/
public class StubbedTest {

    public static void main(String[] args) {
        GetProductInfoCommand getProductInfoCommand = new GetProductInfoCommand(-1L);
        System.out.println(getProductInfoCommand.execute());
    }
}
