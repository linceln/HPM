package com.olsplus.balancemall.app.pay.bean;


import java.io.Serializable;

public class OrderProductEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private OrderProductItemEntity sku_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderProductItemEntity getSku_info() {
        return sku_info;
    }

    public void setSku_info(OrderProductItemEntity sku_info) {
        this.sku_info = sku_info;
    }
}
