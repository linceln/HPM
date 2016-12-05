package com.olsplus.balancemall.app.province.bean;

import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;


public class CityResultEntity extends BaseResultEntity {

    private List<AddressEntity> cities;

    public List<AddressEntity> getCities() {
        return cities;
    }

    public void setCities(List<AddressEntity> cities) {
        this.cities = cities;
    }
}
