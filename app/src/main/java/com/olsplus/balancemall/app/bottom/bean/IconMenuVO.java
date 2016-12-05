package com.olsplus.balancemall.app.bottom.bean;


import java.io.Serializable;

public class IconMenuVO implements Serializable {

    private static final long serialVersionUID = -8116738050140068004L;
    private int defaultIconResId;
    private String iconOff;
    private String iconOn;
    private String name;
    private int redPoint;
    private int showWord;
    private int type;
    private Long updateTime;
    private String url;

    public int getDefaultIconResId() {
        return defaultIconResId;
    }

    public void setDefaultIconResId(int defaultIconResId) {
        this.defaultIconResId = defaultIconResId;
    }

    public String getIconOff() {
        return iconOff;
    }

    public void setIconOff(String iconOff) {
        this.iconOff = iconOff;
    }

    public String getIconOn() {
        return iconOn;
    }

    public void setIconOn(String iconOn) {
        this.iconOn = iconOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRedPoint() {
        return redPoint;
    }

    public void setRedPoint(int redPoint) {
        this.redPoint = redPoint;
    }

    public int getShowWord() {
        return showWord;
    }

    public void setShowWord(int showWord) {
        this.showWord = showWord;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
