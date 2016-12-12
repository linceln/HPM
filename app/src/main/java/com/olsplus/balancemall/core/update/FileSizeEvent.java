package com.olsplus.balancemall.core.update;


public class FileSizeEvent {

    private long currentLen;
    private long contentLen;

    public FileSizeEvent(long currentLen, long contentLen) {
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
