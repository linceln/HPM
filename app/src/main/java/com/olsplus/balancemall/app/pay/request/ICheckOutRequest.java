package com.olsplus.balancemall.app.pay.request;


import com.olsplus.balancemall.app.pay.bean.ShopingOrderSubmitResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingCartEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingPayResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingResultByZero;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingWxPayResult;

import java.util.List;

public interface ICheckOutRequest {

    interface SumitOrderCallback {

        void onSumitOrderSuccess(ShopingOrderSubmitResultEntity shopingOrderSubmitResultEntity);

        void onSumitOrderFailed(String msg);

    }

    interface PayCallback {

        void onPaySuccess(ShoppingPayResultEntity shoppingPayResultEntity);

        void onPayFailed(String msg);

    }

    interface WxPayCallback {

        void onPaySuccess(ShoppingWxPayResult data);

        void onPayFailed(String msg);

    }
    interface SpecialPayCallback {

        void onPaySuccess(ShoppingResultByZero data);

        void onPayFailed(String msg);

    }




    interface GetVoucherCallback {

        void onVoucherSuccess(List<ShoppingVoucherEntity> datas);

        void onVoucherFailed(String msg);

    }

    void sumitOrder(List<ShoppingCartEntity> services );

    void sumitOrder(String orderData);

    void startPay(double totalFee, double needPayFee, int pointsUsed, String voucher, String pay_type, List<String> orders);

    void getVouchers();
}
