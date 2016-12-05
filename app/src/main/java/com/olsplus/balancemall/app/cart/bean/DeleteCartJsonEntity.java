package com.olsplus.balancemall.app.cart.bean;

import java.io.Serializable;
import java.util.List;

public class DeleteCartJsonEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    private String local_service_id;

    private List<DeleteCartItem>  products;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public List<DeleteCartItem> getProducts() {
        return products;
    }

    public void setProducts(List<DeleteCartItem> products) {
        this.products = products;
    }
}
