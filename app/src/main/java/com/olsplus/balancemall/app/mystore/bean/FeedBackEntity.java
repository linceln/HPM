package com.olsplus.balancemall.app.mystore.bean;

import java.io.Serializable;


public class FeedBackEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String platform;
    private String channel;
    private String version;
    private String suggestion;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
