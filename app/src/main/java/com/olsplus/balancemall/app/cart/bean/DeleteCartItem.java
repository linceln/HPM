package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;

public class DeleteCartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String sku_id;

    private String sku_value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getSku_value() {
        return sku_value;
    }

    public void setSku_value(String sku_value) {
        this.sku_value = sku_value;
    }
}
