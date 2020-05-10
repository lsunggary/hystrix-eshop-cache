package com.scott.eshop.cache.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.scott.eshop.cache.degrade.IsDegrade;
import com.scott.eshop.cache.model.ProductInfo;

/**
 * @ClassName GetProductInfoFacadeCommand
 * @Description
 * @Author 47980
 * @Date 2020/5/10 14:18
 * @Version V_1.0
 **/
public class GetProductInfoFacadeCommand extends HystrixCommand<ProductInfo> {

    private Long productId;

    /**
     * 设置为信号量模式，最大量为15
     * @param productId
     */
    public GetProductInfoFacadeCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
        .andCommandKey(HystrixCommandKey.Factory.asKey("ProductInfoFacadeCommand"))
        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
        .withExecutionIsolationSemaphoreMaxConcurrentRequests(15)));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        if (!IsDegrade.isIsDegrade()) {
            return new GetProductInfoCommand(productId).execute();
        } else {
            return new GetProductInfoFromMysqlCommand(productId).execute();
        }
    }

    @Override
    protected ProductInfo getFallback() {
        return new ProductInfo();
    }
}
