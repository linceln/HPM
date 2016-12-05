package com.olsplus.balancemall.core.bean;


import java.io.Serializable;

public class HomePromotionDetail implements Serializable {
    private static final long serialVersionUID = 3211976896117763559L;

    private String bannerPicture;

    private String title;

    private String subTitle;

    private String tag;

    private int titleSrcId;

    private String data;

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerPicture() {
        return bannerPicture;
    }

    public void setBannerPicture(String bannerPicture) {
        this.bannerPicture = bannerPicture;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTitleSrcId() {
        return titleSrcId;
    }

    public void setTitleSrcId(int titleSrcId) {
        this.titleSrcId = titleSrcId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
