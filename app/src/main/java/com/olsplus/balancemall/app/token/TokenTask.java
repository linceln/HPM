package com.olsplus.balancemall.app.token;


import com.olsplus.balancemall.core.app.MyApplication;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenTask {

    public TokenTask() {

    }

    public void updateToken(Subscriber<TokenResult> subscriber) {
        String url = ApiConst.BASE_URL + "v1/token";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(MyApplication.getApp(), SPUtil.UID, "");
        String token = (String) SPUtil.get(MyApplication.getApp(), SPUtil.TOKEN, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);

        HttpManager.getRetrofit()
                .create(TokenService.class)
                .getToken(uid, token, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
