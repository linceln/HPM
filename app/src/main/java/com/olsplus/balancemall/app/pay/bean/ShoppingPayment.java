package com.olsplus.balancemall.app.pay.bean;


import java.io.Serializable;

public class ShoppingPayment implements Serializable{
    private static final long serialVersionUID = 1L;

    private String orderStr;

    public String getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }
}
