package com.olsplus.balancemall.app.mine.bean;


import java.io.Serializable;

public class MyStoreOrderResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private int waitForPay;
    private int waitForRemark;
    private int refund;

    public int getWaitForPay() {
        return waitForPay;
    }

    public void setWaitForPay(int waitForPay) {
        this.waitForPay = waitForPay;
    }

    public int getWaitForRemark() {
        return waitForRemark;
    }

    public void setWaitForRemark(int waitForRemark) {
        this.waitForRemark = waitForRemark;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }
}
