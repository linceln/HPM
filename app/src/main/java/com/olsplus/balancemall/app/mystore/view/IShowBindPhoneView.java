package com.olsplus.balancemall.app.mystore.view;


public interface IShowBindPhoneView {

    void checkPasswordFail(String msg);

    void checkPasswordSuccess();

    void showBindPhoneFail(String msg);

    void showBindPhoneSuccess();

    void showSendsms();

    void showSendsmFailedView(String msg);

}
