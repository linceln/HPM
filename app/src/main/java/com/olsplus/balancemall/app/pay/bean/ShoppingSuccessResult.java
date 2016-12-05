package com.olsplus.balancemall.app.pay.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;


public class ShoppingSuccessResult extends BaseResultEntity {

    private ShoppingTrade trade;

    public ShoppingTrade getTrade() {
        return trade;
    }

    public void setTrade(ShoppingTrade trade) {
        this.trade = trade;
    }
}
