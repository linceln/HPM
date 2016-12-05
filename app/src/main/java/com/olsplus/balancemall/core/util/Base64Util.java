package com.olsplus.balancemall.core.util;


import android.util.Base64;

public class Base64Util {

    /**
     * Base64编码
     *
     * @param str
     */
    public static String encode(String str) {
        return Base64.encodeToString(str.getBytes(), Base64.DEFAULT).replace("=", "");
//                .replace("+", "_a")
//                .replace("/", "_b")
//                .replace("=", "_c");
    }

    /**
     * Base64解码
     *
     * @param str
     */
    public static String decode(String str) {

//        return new String(Base64.decode(str.replace("_a", "+").replace("_b", "/").replace("_c", "="), Base64.DEFAULT));
        return new String(Base64.decode(str, Base64.DEFAULT));
    }
}
