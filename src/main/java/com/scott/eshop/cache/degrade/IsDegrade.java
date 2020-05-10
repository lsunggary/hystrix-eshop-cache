package com.scott.eshop.cache.degrade;

/**
 * @ClassName IsDegrate
 * @Description
 * @Author 47980
 * @Date 2020/5/10 14:22
 * @Version V_1.0
 **/
public class IsDegrade {

    public static boolean isDegrade = false;

    public static boolean isIsDegrade() {
        return isDegrade;
    }

    public static void setIsDegrade(boolean isDegrade) {
        IsDegrade.isDegrade = isDegrade;
    }
}
