package com.scott.eshop.cache.local;

import java.util.HashMap;
import java.util.Map;

/**
 * 品牌缓存
 * @ClassName BrandCache
 * @Description
 * @Author 47980
 * @Date 2020/5/7 22:44
 * @Version V_1.0
 **/
public class BrandCache {

    private static Map<Long, String> brandMap = new HashMap<Long, String>();
    private static Map<Long, Long> productBrandMap = new HashMap<Long, Long>();

    static {
        brandMap.put(1L, "iphone");
        productBrandMap.put(-1L, 1L);
    }

    public static String getBrandName(Long brandId) {
        return brandMap.get(brandId);
    }

    public static Long getProductBrandId(Long productId) {
        return productBrandMap.get(productId);
    }
}
