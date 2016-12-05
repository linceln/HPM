package com.olsplus.balancemall.app.pay.bean;


import java.io.Serializable;
import java.util.List;

public class ShoppingOrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String provider;
    private String order_id;

    private List<ShopingOrderChildItem> suborders;

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

    public List<ShopingOrderChildItem> getSuborders() {
        return suborders;
    }

    public void setSuborders(List<ShopingOrderChildItem> suborders) {
        this.suborders = suborders;
    }
}
