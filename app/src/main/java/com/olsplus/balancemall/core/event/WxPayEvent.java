package com.olsplus.balancemall.core.event;


/**
 * 微信支付回调
 */
public class WxPayEvent {

    private int code;

    public WxPayEvent(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
