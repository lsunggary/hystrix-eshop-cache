package com.scott.eshop.cache.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.scott.eshop.cache.model.ProductInfo;

/**
 * @ClassName GetProductInfoFromMysqlCommand
 * @Description
 * @Author 47980
 * @Date 2020/5/10 14:16
 * @Version V_1.0
 **/
public class GetProductInfoFromMysqlCommand extends HystrixCommand<ProductInfo> {

    private Long productId;

    public GetProductInfoFromMysqlCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FromMysqlCommand")));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        String json = "{\"id\": " + productId + ", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\"}";
        return JSONObject.parseObject(json, ProductInfo.class);
    }
}
