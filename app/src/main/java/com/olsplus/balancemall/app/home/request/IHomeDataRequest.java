package com.olsplus.balancemall.app.home.request;


import com.olsplus.balancemall.app.home.bean.Container;

public interface IHomeDataRequest {

    interface HomeIndexCallback {

        void onGetHomeSuccess(Container container);

        void onGetHomeFailed(String msg);
    }

    void getIndex();

}
