package com.olsplus.balancemall.app.province.bean;



import java.io.Serializable;

public class BuildingAddressEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String addr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
