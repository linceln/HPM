package com.olsplus.balancemall.app.mystore.bean;


import java.io.Serializable;
import java.util.ArrayList;

public class MyOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    private String provider;
    ;
    private String order_id;
    private double total;
    private double pay;
    private String pointPay;
    private String status;
    private String refund_status;
    private String created_time;
    private String trade_id;
    private String pay_time;

    private ArrayList<MyOrderInfo> suborders;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public String getPointPay() {
        return pointPay;
    }

    public void setPointPay(String pointPay) {
        this.pointPay = pointPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getTrade_id() {
        return trade_id;
    }

    public void setTrade_id(String trade_id) {
        this.trade_id = trade_id;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public ArrayList<MyOrderInfo> getSuborders() {
        return suborders;
    }

    public void setSuborders(ArrayList<MyOrderInfo> suborders) {
        this.suborders = suborders;
    }
}
