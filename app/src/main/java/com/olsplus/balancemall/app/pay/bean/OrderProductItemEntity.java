package com.olsplus.balancemall.app.pay.bean;


import java.io.Serializable;

public class OrderProductItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sku_value;

    private double price;

    private int count;

    private String sku_id;

    public String getSku_value() {
        return sku_value;
    }

    public void setSku_value(String sku_value) {
        this.sku_value = sku_value;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }
}
