package com.olsplus.balancemall.core.bean;

import java.io.Serializable;

/**
 * 回调信息统一封装类
 */
public class BaseResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //  判断标示
    private int ret;
    //    提示信息
    private String msg;

    private String strToSign;

    private String serverSign;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

}
