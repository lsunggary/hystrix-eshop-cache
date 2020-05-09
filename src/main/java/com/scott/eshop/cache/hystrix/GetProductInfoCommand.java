package com.scott.eshop.cache.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.*;
import com.scott.eshop.cache.model.ProductInfo;
import com.scott.eshop.cache.utils.HttpClientUtils;

/**
 * @ClassName GetProductInfoCommand
 * @Description
 * @Author 47980
 * @Date 2020/5/7 20:38
 * @Version V_1.0
 **/
public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {

    private Long productId;

    public GetProductInfoCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfoCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetProductInfoPool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(10)
                        .withMaxQueueSize(10)
                        .withQueueSizeRejectionThreshold(8)
//                        .withAllowMaximumSizeToDivergeFromCoreSize(true)
                )
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerRequestVolumeThreshold(30)
                        .withCircuitBreakerErrorThresholdPercentage(40)
                        .withCircuitBreakerSleepWindowInMilliseconds(3000)
                        .withExecutionTimeoutInMilliseconds(400)
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(50))
        );
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        System.out.println("调用接口，查询商品数据，productId=" + productId);

        if(productId.equals(-1L)) {
            throw new Exception();
        }

        if(productId.equals(-2L)) {
            Thread.sleep(3000);
        }

        if (productId.equals(-3L)) {
//            Thread.sleep(100);
        }

        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        return JSONObject.parseObject(response, ProductInfo.class);
    }

//    @Override
//    protected String getCacheKey() {
//        return "product_info_" + productId;
//    }


    @Override
    protected ProductInfo getFallback() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("降级商品");
        return productInfo;
    }
}
