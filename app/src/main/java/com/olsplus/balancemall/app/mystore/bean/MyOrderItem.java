package com.olsplus.balancemall.app.mystore.bean;

import java.io.Serializable;
import java.util.ArrayList;


public class MyOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String provider;
    private String order_id;
    private double total;
    private String pay;
    private String status;
    private String refund_status;

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

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
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

    public ArrayList<MyOrderInfo> getSuborders() {
        return suborders;
    }

    public void setSuborders(ArrayList<MyOrderInfo> suborders) {
        this.suborders = suborders;
    }
}
