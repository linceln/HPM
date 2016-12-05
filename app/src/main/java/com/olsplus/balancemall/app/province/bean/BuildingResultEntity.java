package com.olsplus.balancemall.app.province.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

import java.util.List;

public class BuildingResultEntity extends BaseResultEntity{

    private List<BuildingAddressEntity> buildings;

    public List<BuildingAddressEntity> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<BuildingAddressEntity> buildings) {
        this.buildings = buildings;
    }
}
