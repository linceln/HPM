package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;
import java.util.List;

public class DeleteCartRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DeleteCartJsonEntity> cart;

    public List<DeleteCartJsonEntity> getCart() {
        return cart;
    }

    public void setCart(List<DeleteCartJsonEntity> cart) {
        this.cart = cart;
    }
}
