package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;
import java.util.List;

public class ShoppingItemGroup implements Serializable {
    private static final long serialVersionUID = 7354715381405289288L;

    private String local_service_id;
    private String local_service_name;
    private List<ShoppingCartItem> products;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public String getLocal_service_name() {
        return local_service_name;
    }

    public void setLocal_service_name(String local_service_name) {
        this.local_service_name = local_service_name;
    }

    public List<ShoppingCartItem> getProducts() {
        return products;
    }

    public void setProducts(List<ShoppingCartItem> products) {
        this.products = products;
    }
}
