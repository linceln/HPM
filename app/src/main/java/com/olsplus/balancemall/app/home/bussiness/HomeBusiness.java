package com.olsplus.balancemall.app.home.bussiness;


import android.content.Context;

import com.olsplus.balancemall.app.home.bean.Container;
import com.olsplus.balancemall.app.home.request.HomeIndexService;
import com.olsplus.balancemall.app.home.request.IHomeDataRequest;
import com.olsplus.balancemall.core.http.DoTransform;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.SPUtil;

public class HomeBusiness {

    private Context context;

    public HomeBusiness(Context context) {
        this.context = context;
    }

    /**
     * @param callback
     */
    public void getHomeIndex(final IHomeDataRequest.HomeIndexCallback callback) {

        String building_id = (String) SPUtil.get(context, SPUtil.BUILDING_ID, "");
        HttpResultObserver respObserver = new HttpResultObserver<Container>() {

            @Override
            public void onReconnect() {
                getHomeIndex(callback);
            }

            @Override
            public void onSuccess(Container data) {
                if (data == null) {
                    callback.onGetHomeFailed("没有数据哦");
                    return;
                }
                callback.onGetHomeSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                callback.onGetHomeFailed(msg);
            }
        };

        HttpManager.getRetrofit()
                .create(HomeIndexService.class)
                .getHomeIndex(building_id, HttpUtil.getUid(), HttpUtil.getToken(), HttpUtil.getTimestamp())
                .compose(DoTransform.<Container>applyScheduler())
                .subscribe(respObserver);
    }
}
