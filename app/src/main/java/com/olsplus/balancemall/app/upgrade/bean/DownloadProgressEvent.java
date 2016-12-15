package com.olsplus.balancemall.app.upgrade.bean;


public class DownloadProgressEvent {

    private long currentLen;
    private long contentLen;

    public DownloadProgressEvent(long currentLen, long contentLen) {
        this.currentLen = currentLen;
        this.contentLen = contentLen;
    }

    public long getCurrentLen() {
        return currentLen;
    }

    public void setCurrentLen(long currentLen) {
        this.currentLen = currentLen;
    }

    public long getContentLen() {
        return contentLen;
    }

    public void setContentLen(long contentLen) {
        this.contentLen = contentLen;
    }
}
