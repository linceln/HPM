package com.olsplus.balancemall.app.cart.bean;


import java.io.Serializable;

public class AddToCartResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private AddCartRequest addtocart;

    public AddCartRequest getAddtocart() {
        return addtocart;
    }

    public void setAddtocart(AddCartRequest addtocart) {
        this.addtocart = addtocart;
    }
}
