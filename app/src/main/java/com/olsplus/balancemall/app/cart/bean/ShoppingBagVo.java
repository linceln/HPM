package com.olsplus.balancemall.app.cart.bean;

import java.io.Serializable;
import java.util.List;

public class ShoppingBagVo implements Serializable {
    private static final long serialVersionUID = 525645432762769987L;

    private double sum;
    private int cart_count;
    private List<ShoppingItemGroupVo> cart_list;

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getCart_count() {
        return cart_count;
    }

    public void setCart_count(int cart_count) {
        this.cart_count = cart_count;
    }

    public List<ShoppingItemGroupVo> getCart_list() {
        return cart_list;
    }

    public void setCart_list(List<ShoppingItemGroupVo> cart_list) {
        this.cart_list = cart_list;
    }
}
