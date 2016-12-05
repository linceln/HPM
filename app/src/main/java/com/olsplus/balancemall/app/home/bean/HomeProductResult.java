package com.olsplus.balancemall.app.home.bean;


import java.io.Serializable;
import java.util.List;

public class HomeProductResult implements Serializable
{
    private static final long serialVersionUID = 6056801186438023558L;

    private List<PromoteTopic> services;

    private String title;

    public List<PromoteTopic> getServices() {
        return services;
    }

    public void setServices(List<PromoteTopic> services) {
        this.services = services;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
