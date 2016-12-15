package com.olsplus.balancemall.app.mine.view;


public interface IReturnOrderView {

    void showReturnOrderFailedView(String msg);

    void showReturnOrderView();

    void updateReturnImgFail(String msg,int position);

    void updateReturnImgNext(String url);

    void updateReturnImgCompleted();
}
