package com.scott.eshop.cache;

import com.scott.eshop.cache.utils.HttpClientUtils;

/**
 * @ClassName RequestCollapserTest
 * @Description
 * @Author 47980
 * @Date 2020/5/9 21:25
 * @Version V_1.0
 **/
public class RequestCollapserTest {
    public static void main(String[] args) {
        HttpClientUtils.sendGetRequest("http://localhost:8082/getProductInfos?productIds=1,2,3,1,1,2,2,3,4");
    }
}
