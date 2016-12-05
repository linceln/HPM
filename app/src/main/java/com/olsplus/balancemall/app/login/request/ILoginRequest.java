package com.olsplus.balancemall.app.login.request;


import com.olsplus.balancemall.app.login.bean.LoginResultEntity;

public interface ILoginRequest {

    interface LoginCallback {

        void onLoginSuccess(LoginResultEntity loginResultEntity);

        void onLoginFailed(String msg);

    }

    interface LoginOutCallback {

        void onLoginOutSuccess();

        void onLoginOutFailed(String msg);
    }

    interface RegisterCallback {

        void onRegisterSuccess();

        void onRegisterFailed(String msg);
    }

    interface SendsmsCallback {

        void onSendSuccess();

        void onSendFailed(String msg);
    }

    interface ForgetPwdCallback {

        void onShowForgetPwdSuccess();

        void onShowForgetPwdFailed(String msg);
    }


    void login(String name, String password);

    void register(String phone, String name, String password);

    void sendSms(String phone);

    void sendForgetPwdSms(String phone);

    void forgetPwd(String phone,String pwd);

    void loginOut();
}
