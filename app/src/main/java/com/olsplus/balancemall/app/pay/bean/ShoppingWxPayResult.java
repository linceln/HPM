package com.olsplus.balancemall.app.pay.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class ShoppingWxPayResult extends BaseResultEntity {

    private ShoppingWxPayment wechat;

    public ShoppingWxPayment getWechat() {
        return wechat;
    }

    public void setWechat(ShoppingWxPayment wechat) {
        this.wechat = wechat;
    }

    @Override
    public String toString() {
        return "ShoppingWxPayResult{" +
                "wechat=" + wechat +
                '}';
    }
}
