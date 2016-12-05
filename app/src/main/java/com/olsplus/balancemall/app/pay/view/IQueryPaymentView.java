package com.olsplus.balancemall.app.pay.view;


import com.olsplus.balancemall.app.pay.bean.ShoppingSuccessResult;

public interface IQueryPaymentView {

    void showQueryPaymentFailed(String msg);

    void showQueryPaymentSuccess(ShoppingSuccessResult shoppingSuccessResult);
}
