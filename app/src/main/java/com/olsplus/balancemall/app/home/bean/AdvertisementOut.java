package com.olsplus.balancemall.app.home.bean;


import java.io.Serializable;

public class AdvertisementOut implements Serializable {
    private static final long serialVersionUID = 1L;

    private String img;

    private String url;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
