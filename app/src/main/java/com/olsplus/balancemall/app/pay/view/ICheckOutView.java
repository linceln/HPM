package com.olsplus.balancemall.app.pay.view;

import com.olsplus.balancemall.app.pay.bean.ShopingOrderSubmitResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingPayResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingResultByZero;
import com.olsplus.balancemall.app.pay.bean.ShoppingWxPayResult;
import com.olsplus.balancemall.core.util.BaseView;


public interface ICheckOutView extends BaseView {

    void showSumitOrderFailed(String msg);

    void onSumitOrderSuccess(ShopingOrderSubmitResultEntity shopingOrderSubmitResultEntity);

    void showPayFailed(String msg);

    void showPaySuccess(ShoppingPayResultEntity shoppingPayResultEntity);

    void showWxPaySuccess(ShoppingWxPayResult shoppingWxPayResult);

    void showSpecialPaySuccess(ShoppingResultByZero shoppingResultByZero);
}
