package com.scott.eshop.cache;

import com.scott.eshop.cache.utils.HttpClientUtils;

/**
 * @ClassName TimeoutTest
 * @Description
 * @Author 47980
 * @Date 2020/5/9 20:01
 * @Version V_1.0
 **/
public class TimeoutTest {

    public static void main(String[] args) {
        String response = HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfo?productId=-3");
    }

}
