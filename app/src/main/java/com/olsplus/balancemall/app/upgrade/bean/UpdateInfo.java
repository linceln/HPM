package com.olsplus.balancemall.app.upgrade.bean;


import java.io.Serializable;

public class UpdateInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String number;
    private String url;
    private String info;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
