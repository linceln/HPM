package com.olsplus.balancemall.app.pay.bean;


import java.io.Serializable;

public class ShopingPayQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pay_type;
    private String trade_id;
    private String thirdparty_trade_id;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getThirdparty_trade_id() {
        return thirdparty_trade_id;
    }

    public void setThirdparty_trade_id(String thirdparty_trade_id) {
        this.thirdparty_trade_id = thirdparty_trade_id;
    }
}
