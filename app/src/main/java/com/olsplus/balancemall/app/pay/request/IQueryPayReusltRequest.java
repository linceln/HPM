package com.olsplus.balancemall.app.pay.request;

import com.olsplus.balancemall.app.pay.bean.ShoppingSuccessResult;

public interface IQueryPayReusltRequest {

    interface QueryCallback {

        void onQueryPayReusltSuccess(ShoppingSuccessResult data);

        void onQueryPayReusltFailed(String msg);

    }

    void queryPayMent();

}
