package com.scott.eshop.cache;

import com.scott.eshop.cache.utils.HttpClientUtils;

/**
 * @ClassName CircuitBreakTest
 * @Description
 * @Author 47980
 * @Date 2020/5/8 20:58
 * @Version V_1.0
 **/
public class CircuitBreakTest {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 15; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId="+i);
            System.out.println("第" + ( i + 1 ) + "此请求，结果为：" + response);
        }
        for (int i = 0; i < 25; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=-1");
            System.out.println("第" + ( i + 1 ) + "此请求，结果为：" + response);
        }
        Thread.sleep(5000);
        // 等待了5s后，时间窗口统计了，发现异常比例太高，就短路了。
        for (int i = 0; i < 15; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=-1");
            System.out.println("第" + ( i + 1 ) + "此请求，结果为：" + response);
        }
        System.out.println("============================ 尝试等待3秒钟 ============================");
        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
            String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId="+i);
            System.out.println("第" + ( i + 1 ) + "此请求，结果为：" + response);
        }
    }
}
