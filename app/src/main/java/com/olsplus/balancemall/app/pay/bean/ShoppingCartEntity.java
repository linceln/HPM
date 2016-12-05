package com.olsplus.balancemall.app.pay.bean;

import java.io.Serializable;
import java.util.List;

public class ShoppingCartEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String local_service_id;

    private List<ShoppingProductEntity> products;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public List<ShoppingProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ShoppingProductEntity> products) {
        this.products = products;
    }
}
