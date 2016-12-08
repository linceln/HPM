package com.olsplus.balancemall.core.http;


import com.olsplus.balancemall.core.app.MyApplication;
import com.olsplus.balancemall.core.bean.TokenResult;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpdateTokenTask {

    public  UpdateTokenTask(){

    }

    public void updateToken(final UpdateCallBack callBack){
        String url = ApiConst.BASE_URL + "v1/token";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(MyApplication.getApp(), SPUtil.UID, "");
        String token = (String) SPUtil.get(MyApplication.getApp(), SPUtil.TOKEN, "");
        String sign = parseUpdateTokenSign(url, uid, token,timestamp);
        HttpManager.getRetrofit()
                .create(TokenService.class)
                .getToken(uid,token,timestamp,sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TokenResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.updateFailed();
                    }

                    @Override
                    public void onNext(TokenResult tokenResult) {
                        if (tokenResult == null) {
                            callBack.updateFailed();
                            return;
                        }
                        if(tokenResult.getRet() ==0){
                            String token = tokenResult.getToken();
                            SPUtil.put(MyApplication.getApp(),SPUtil.TOKEN,token);
//                            reConnect();
                            callBack.updateSuccess();
                        }else{
                            callBack.updateFailed();

                        }

                    }
                });
    }

    private String parseUpdateTokenSign(String url, String uid, String token,String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }


}
