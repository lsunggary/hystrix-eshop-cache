package com.scott.eshop.cache.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.*;
import com.scott.eshop.cache.local.BrandCache;
import com.scott.eshop.cache.local.LocationCache;
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
                        .withCoreSize(3)
                        .withMaximumSize(30)
                        .withAllowMaximumSizeToDivergeFromCoreSize(true)
                        .withKeepAliveTimeMinutes(1)
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
//        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService")));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
//        System.out.println("调用接口，查询商品数据，productId=" + productId);
//
        if(productId.equals(-1L)) {
            throw new Exception();
        }

        if(productId.equals(-2L)) {
            Thread.sleep(3000);
        }
//
//        if (productId.equals(-3L)) {
////            Thread.sleep(100);
//        }

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
        return new FirstFallbackCommand(productId).execute();
    }

    private static class FirstFallbackCommand extends HystrixCommand<ProductInfo> {

        private Long productId;

        public FirstFallbackCommand (Long productId) {
            // 需要单独有线程池，不然主线程没有线程了，就会被一直卡住
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("FirstFallbackCommand"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("FirstLevelFallbackPool")));
            this.productId = productId;
        }

        @Override
        protected ProductInfo run() throws Exception {
            if (productId.equals(-2L)) {
                throw new Exception();
            }
            String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
            String response = HttpClientUtils.sendGetRequest(url);
            return JSONObject.parseObject(response, ProductInfo.class);
        }

        /**
         * 第二级降级策略
         * @return
         */
        @Override
        protected ProductInfo getFallback() {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(productId); // 从请求参数中获取到的唯一值
            productInfo.setBrandId(BrandCache.getProductBrandId(productId));
            productInfo.setBrandName(BrandCache.getBrandName(productInfo.getBrandId()));
            productInfo.setCityId(LocationCache.getProductCityId(productId));
            productInfo.setCityName(LocationCache.getCityName(productInfo.getCityId()));
            productInfo.setColor("默认颜色");
            productInfo.setModifiedTime("2020-05-06 12:00:00");
            productInfo.setPictureList("http://url");
            productInfo.setSpecification("默认规格");
            productInfo.setService("默认服务");
            productInfo.setPrice(100.00);
            productInfo.setName("降级商品");
            return productInfo;
        }
    }
}
