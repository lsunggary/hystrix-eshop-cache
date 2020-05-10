package com.scott.eshop.cache;

import com.scott.eshop.cache.degrade.IsDegrade;
import com.scott.eshop.cache.hystrix.GetProductInfoFacadeCommand;

/**
 * @ClassName ManuaDegradeTest
 * @Description
 * @Author 47980
 * @Date 2020/5/10 14:27
 * @Version V_1.0
 **/
public class ManuaDegradeTest {

    public static void main(String[] args) {
        GetProductInfoFacadeCommand getProductInfoFacadeCommand1 = new GetProductInfoFacadeCommand(-1L);
        System.out.println(getProductInfoFacadeCommand1.execute());

        IsDegrade.setIsDegrade(true);

        GetProductInfoFacadeCommand getProductInfoFacadeCommand2 = new GetProductInfoFacadeCommand(2L);
        System.out.println(getProductInfoFacadeCommand2.execute());
    }
}
