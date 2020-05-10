package com.scott.eshop.cache;

import com.scott.eshop.cache.hystrix.GetProductInfoCommand;

/**
 * @ClassName MultiLevelFallbackTest
 * @Description
 * @Author 47980
 * @Date 2020/5/10 14:09
 * @Version V_1.0
 **/
public class MultiLevelFallbackTest {

    public static void main(String[] args) {
        GetProductInfoCommand getProductInfoCommand1 = new GetProductInfoCommand(-1L);
        System.out.println(getProductInfoCommand1.execute());

        GetProductInfoCommand getProductInfoCommand2 = new GetProductInfoCommand(-2L);
        System.out.println(getProductInfoCommand2.execute());
    }
}
