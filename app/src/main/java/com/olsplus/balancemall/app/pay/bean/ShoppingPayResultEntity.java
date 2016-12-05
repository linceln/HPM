package com.olsplus.balancemall.app.pay.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class ShoppingPayResultEntity extends BaseResultEntity {

    private String trade_id;

    private ShoppingPayment alipay;

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public ShoppingPayment getAlipay() {
        return alipay;
    }

    public void setAlipay(ShoppingPayment alipay) {
        this.alipay = alipay;
    }
}
