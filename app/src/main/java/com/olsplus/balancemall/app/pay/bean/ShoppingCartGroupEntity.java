package com.olsplus.balancemall.app.pay.bean;

import java.io.Serializable;
import java.util.List;


public class ShoppingCartGroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ShoppingCartEntity> services;

    public List<ShoppingCartEntity> getServices() {
        return services;
    }

    public void setServices(List<ShoppingCartEntity> services) {
        this.services = services;
    }
}
