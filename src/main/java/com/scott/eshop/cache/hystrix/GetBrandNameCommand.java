package com.scott.eshop.cache.hystrix;

import com.netflix.hystrix.*;
import com.scott.eshop.cache.local.BrandCache;
import com.scott.eshop.cache.model.ProductInfo;

import java.util.Map;

/**
 * 获取品牌名称的command
 * @ClassName GetBrandNameCommand
 * @Description
 * @Author 47980
 * @Date 2020/5/7 22:35
 * @Version V_1.0
 **/
public class GetBrandNameCommand extends HystrixCommand<String> {

    private Long brandId;

    public GetBrandNameCommand(Long brandId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BrandInfoService"))
        .andCommandKey(HystrixCommandKey.Factory.asKey("GetBrandNameCommand"))
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetBrandInfoPool"))
        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                // 最大线程数
                .withCoreSize(15)
                // 当线程满后的最大队列数
                .withMaxQueueSize(25)
                .withQueueSizeRejectionThreshold(20))
        .andCommandPropertiesDefaults(
                // fallback 限流
                HystrixCommandProperties.Setter()
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(100)
        )
        );
        this.brandId = brandId;
    }

    @Override
    protected String run() throws Exception {
        throw new Exception();
    }

    @Override
    protected String getFallback() {
        System.out.println("从本地缓存获取过期的品牌数据： brandId="+brandId);
        return BrandCache.getBrandName(brandId);
    }
}
