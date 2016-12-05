package com.olsplus.balancemall.app.home.bussiness;


import android.content.Context;

import com.olsplus.balancemall.app.home.bean.Container;
import com.olsplus.balancemall.app.home.request.HomeIndexService;
import com.olsplus.balancemall.app.home.request.IHomeDataRequest;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetHomeIndexBussiness {

    private Context context;

    public GetHomeIndexBussiness(Context context) {
        this.context = context;
    }

    /**
     *
     * @param callback
     */
    public void getHomeIndex(final IHomeDataRequest.HomeIndexCallback callback) {
        String url = ApiConst.BASE_URL + "v1/index";
        String building_id = (String) SPUtil.get(context, SPUtil.BUILDING_ID, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseHomeIndexSign(building_id,url, uid, token, timestamp);

        HttpResultObserver respObserver = new HttpResultObserver<Container>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                getHomeIndex(callback);
            }

            @Override
            public void handleSuccessResp(Container data) {
                if (data == null) {
                    callback.onGetHomeFailed("数据出错了");
                    return;
                }
                callback.onGetHomeSuccess(data);
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "getHomeIndex failed");
                callback.onGetHomeFailed(msg);
            }
        };

        HttpManager.getRetrofit()
                .create(HomeIndexService.class)
                .getHomeIndex(building_id,uid,token,timestamp,sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 生成首页数据签名
     * @param building_id
     * @param url
     * @param uid
     * @param token
     * @return
     */
    private String parseHomeIndexSign(String building_id,String url, String uid, String token,String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("building_id", building_id);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

}
