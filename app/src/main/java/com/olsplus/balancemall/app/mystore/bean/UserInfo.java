package com.olsplus.balancemall.app.mystore.bean;


import java.io.Serializable;

public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String points;
    private String avatar;
    private String gender;
    private String building_id;
    private String building_name;
    private String local_service_id;
    private String local_service_name;

    public String getLocal_service_id() {
        return local_service_id;
    }

    public void setLocal_service_id(String local_service_id) {
        this.local_service_id = local_service_id;
    }

    public String getLocal_service_name() {
        return local_service_name;
    }

    public void setLocal_service_name(String local_service_name) {
        this.local_service_name = local_service_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

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
