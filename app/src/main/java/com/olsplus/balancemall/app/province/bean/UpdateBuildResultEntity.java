package com.olsplus.balancemall.app.province.bean;


import com.olsplus.balancemall.core.bean.BaseResultEntity;

public class UpdateBuildResultEntity extends BaseResultEntity{

    private  String building_id;

    private String building_name;

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }
}
