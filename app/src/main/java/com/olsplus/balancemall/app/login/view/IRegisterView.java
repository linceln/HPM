package com.olsplus.balancemall.app.login.view;


import com.olsplus.balancemall.core.util.BaseView;

public interface IRegisterView extends BaseView {

    void showRegisterError(String msg);

    void onRegisterSuccess();

    void showSendsmsFailedView(String msg);

    void showSendsmsSuccess();

}
