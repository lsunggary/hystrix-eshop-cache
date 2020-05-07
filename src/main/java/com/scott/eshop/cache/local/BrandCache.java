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

    static {
        brandMap.put(1L, "iphone");
    }

    public static String getBrandName(Long brandId) {
        return brandMap.get(brandId);
    }

}
