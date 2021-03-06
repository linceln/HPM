package com.olsplus.balancemall.core.http;


import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.core.app.MyApplication;
import com.olsplus.balancemall.core.util.EncrypUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static String POST = "POST";

    public static String GET = "GET";
    /**
     * 平台类型
     */
    public static String PLATFORM = "2";
    /**
     * 渠道名称
     */
    public static String CHANNEL = BuildConfig.FLAVOR;
    private static String SECRET = "HePingMao*&@-_";

    public static String getUid() {
        return (String) SPUtil.get(MyApplication.getApp(), SPUtil.UID, "");
    }

    public static String getToken() {
        return (String) SPUtil.get(MyApplication.getApp(), SPUtil.TOKEN, "");
    }

    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getLocalServiceId() {
        return (String) SPUtil.get(MyApplication.getApp(), SPUtil.LOCAL_SERVICE_ID, "");
    }

    public static String sign(String method, String url, Map<String, String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(method)) {
            stringBuffer.append(method);
            stringBuffer.append("&");
        }
        if (!TextUtils.isEmpty(url)) {
            stringBuffer.append(url);
            stringBuffer.append("&");
        }
        if (map != null) {
            Collection<String> keySet = map.keySet();
            List<String> list = new ArrayList<>(keySet);
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.isEmpty(list.get(i))) {
                    stringBuffer.append(map.get(list.get(i)));
                } else {
                    stringBuffer.append(list.get(i) + "=" + map.get(list.get(i)));
                }
                stringBuffer.append("&");
            }
        }
        String secret = SECRET.substring(0, SECRET.length() - 2) + "+/";
        stringBuffer.append(secret);
        String signStr = stringBuffer.toString();
//        LogUtil.e("Sign String", signStr);
        if (!TextUtils.isEmpty(signStr)) {

            String signData = EncrypUtil.eccryptSHA1(signStr);
            return signData;
        }
        return null;
    }

    @Nullable
    public static String signWithJson(String method, String url, Map<String, String> map, String json) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(method)) {
            stringBuffer.append(method);
            stringBuffer.append("&");
        }
        if (!TextUtils.isEmpty(url)) {
            stringBuffer.append(url);
            stringBuffer.append("&");
        }
        if (map != null) {
            Collection<String> keySet = map.keySet();
            List<String> list = new ArrayList<>(keySet);
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                stringBuffer.append(list.get(i) + "=" + map.get(list.get(i)));
                stringBuffer.append("&");
            }
        }
        if (!TextUtils.isEmpty(json)) {
            stringBuffer.append(json);
            stringBuffer.append("&");
        }
        String secret = SECRET.substring(0, SECRET.length() - 2) + "+/";
        stringBuffer.append(secret);
        String signStr = stringBuffer.toString();
//        LogUtil.applyScheduler("Sign String with Json", signStr);
        if (!TextUtils.isEmpty(signStr)) {
            String signData = EncrypUtil.eccryptSHA1(signStr);
            return signData;
        }
        return null;
    }
}
