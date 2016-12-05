package com.olsplus.balancemall.app.cart.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;

public class ShoppingBag extends BaseResultEntity{

    private List<ShoppingItemGroup> cart;

    public List<ShoppingItemGroup> getCart() {
        return cart;
    }

    public void setCart(List<ShoppingItemGroup> cart) {
        this.cart = cart;
    }
}
