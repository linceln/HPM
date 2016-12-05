package com.olsplus.balancemall.app.home.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;

public class Container extends BaseResultEntity {


    private List<AdvertisementOut> ads;

    private List<PromoteTopic> services;

    public List<AdvertisementOut> getAds() {
        return ads;
    }

    public void setAds(List<AdvertisementOut> ads) {
        this.ads = ads;
    }

    public List<PromoteTopic> getServices() {
        return services;
    }

    public void setServices(List<PromoteTopic> services) {
        this.services = services;
    }
}
