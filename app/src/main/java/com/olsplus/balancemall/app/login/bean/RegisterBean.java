package com.olsplus.balancemall.app.login.bean;


import java.io.Serializable;

public class RegisterBean implements Serializable {
    private String mobile;//手机号
    private String nickname;//昵称
    private String password;//密码


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
