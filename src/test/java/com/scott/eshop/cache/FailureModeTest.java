package com.scott.eshop.cache;

import com.scott.eshop.cache.hystrix.FailureModeCommand;

/**
 * @ClassName FailureModeTest
 * @Description
 * @Author 47980
 * @Date 2020/5/9 22:04
 * @Version V_1.0
 **/
public class FailureModeTest {

    public static void main(String[] args) {

        try {
            FailureModeCommand failureModeCommand = new FailureModeCommand(true);
            System.out.println(failureModeCommand.execute());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
