package com.olsplus.balancemall.app.pay.bean;


import java.io.Serializable;
import java.util.List;

public class ShoppingTrade implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tid;
    private double total;
    private String pointsUsed;
    private String pay;
    private String voucherUsed;
    private List<ShoppingOrderItem> orders;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getVoucherUsed() {
        return voucherUsed;
    }

    public void setVoucherUsed(String voucherUsed) {
        this.voucherUsed = voucherUsed;
    }

    public String getPointsUsed() {
        return pointsUsed;
    }

    public void setPointsUsed(String pointsUsed) {
        this.pointsUsed = pointsUsed;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public List<ShoppingOrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<ShoppingOrderItem> orders) {
        this.orders = orders;
    }
}
