package com.olsplus.balancemall.app.pay.business;


import android.content.Context;

import com.olsplus.balancemall.app.pay.bean.ShoppingSuccessResult;
import com.olsplus.balancemall.app.pay.request.IQueryPayReusltRequest;
import com.olsplus.balancemall.app.pay.request.OrderService;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QueryPayReusltBussiness {
    private Context context;

    public QueryPayReusltBussiness(Context context) {
        this.context = context;
    }


    /**
     * 订单支付结果查询
     * @param json
     * @param callback
     */
    public void queryPayment(final String json,final IQueryPayReusltRequest.QueryCallback callback){
        String url = ApiConst.BASE_URL + "v1/payment/query";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseQuerySign(url, timestamp,uid, token,json);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        String requestUrl = "v1/payment/query?uid="+uid+"&token="+token+"&timestamp="+timestamp+"&sign="+sign;
        HttpResultObserver respObserver = new HttpResultObserver<ShoppingSuccessResult>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                queryPayment(json, callback);
            }

            @Override
            public void onSuccess(ShoppingSuccessResult data) {
                if (data == null) {
                    callback.onQueryPayReusltFailed("数据出错了");
                    return;
                }
                callback.onQueryPayReusltSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("yongyuan.w", "payResultQuery cart failed");
                callback.onQueryPayReusltFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(OrderService.class)
                .payResultQuery(requestUrl,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    private String parseQuerySign(String url, String timestamp,String uid,String token,String json){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.signWithJson(HttpUtil.POST, url, paramMap,json);
        return sign;
    }

}
