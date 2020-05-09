package com.scott.eshop.cache.hystrix;

import com.alibaba.fastjson.JSONArray;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.scott.eshop.cache.model.ProductInfo;
import com.scott.eshop.cache.utils.HttpClientUtils;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName GetProductInfosCollapser
 * @Description
 * @Author 47980
 * @Date 2020/5/9 20:32
 * @Version V_1.0
 **/
public class GetProductInfosCollapser extends HystrixCollapser<List<ProductInfo>, ProductInfo, Long> {

    private Long productId;

    public GetProductInfosCollapser (Long productId) {
        this.productId = productId;
    }

    @Override
    public Long getRequestArgument() {
        return productId;
    }

    @Override
    protected HystrixCommand<List<ProductInfo>> createCommand(Collection<CollapsedRequest<ProductInfo, Long>> collection) {
        StringBuffer paramsBuilder = new StringBuffer();

        for (CollapsedRequest<ProductInfo, Long> request: collection) {
            paramsBuilder.append(request.getArgument()).append(",");
        }
        String params = paramsBuilder.toString();
        params = params.substring(0, params.length() -1);

        System.out.println("createCommand 方法执行：params=" + params);
        return new BatchCommand(collection);
    }

    /**
     * 将请求参数与请求结果做映射
     * @param productInfos
     * @param collection
     */
    @Override
    protected void mapResponseToRequests(List<ProductInfo> productInfos, Collection<CollapsedRequest<ProductInfo, Long>> collection) {
        int count = 0;
        for (CollapsedRequest<ProductInfo, Long> request: collection) {
            request.setResponse(productInfos.get(count++));
        }

    }

    @Override
    protected String getCacheKey() {
        return "product_info_" + productId;
    }

    private static final class BatchCommand extends HystrixCommand<List<ProductInfo>> {

        public final Collection<CollapsedRequest<ProductInfo, Long>> requests;

        public BatchCommand(Collection<CollapsedRequest<ProductInfo, Long>> requests) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoServer"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfosCollapserBatchCommand")));
            this.requests = requests;
        }

        @Override
        protected List<ProductInfo> run() throws Exception {
            StringBuffer paramsBuilder = new StringBuffer();

            for (CollapsedRequest<ProductInfo, Long> request: requests) {
                paramsBuilder.append(request.getArgument()).append(",");
            }
            String params = paramsBuilder.toString();
            params = params.substring(0, params.length() -1);

            // 将多个商品id合并在一个batch内，发送一次网络请求，获取到多个商品信息

            String url = "http://localhost:8083/getProductInfos?productIds="+params;
            String response = HttpClientUtils.sendGetRequest(url);

            List<ProductInfo> productInfos = JSONArray.parseArray(response, ProductInfo.class);

            productInfos.forEach(productInfo -> {
                System.out.println("BatchCommand 内部， productInfo=" + productInfo);
            });

            return productInfos;
        }
    }
}
