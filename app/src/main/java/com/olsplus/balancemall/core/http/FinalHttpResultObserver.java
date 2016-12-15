package com.olsplus.balancemall.core.http;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

public class FinalHttpResultObserver<T extends BaseResultEntity> extends HttpResultObserver<T> {

    private RequestCallback<T> callback;

    public FinalHttpResultObserver(RequestCallback<T> callback){

        this.callback = callback;
    }

    @Override
    public void onSuccess(T data) {
        if(callback != null){
            callback.onSuccess(data);
        }
    }

    @Override
    public void onFail(String msg) {
        if(callback != null){
            callback.onError(msg);
        }
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onReconnect() {

    }
}
