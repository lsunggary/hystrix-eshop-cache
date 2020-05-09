package com.scott.eshop.cache;

import com.scott.eshop.cache.utils.HttpClientUtils;

/**
 * @ClassName CircuitBreakTest
 * @Description
 * @Author 47980
 * @Date 2020/5/8 20:58
 * @Version V_1.0
 **/
public class RejectTest {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 25; i++) {
            new TestThread(-2L).start();
        }
    }

    public static class TestThread extends Thread {

        private Long productId;

        public TestThread(Long productId) {
            this.productId = productId;
        }

        @Override
        public void run() {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId="+productId);
            System.out.println("===== 结果为 ===== ：" + response);
        }
    }
}
