package com.olsplus.balancemall.app.login.view;


import com.olsplus.balancemall.app.login.bean.LoginResultEntity;
import com.olsplus.balancemall.core.util.BaseView;

public interface ILoginView extends BaseView{

    void showLoginError(String msg);

    void onLoginSuccess(LoginResultEntity loginResultEntity);

}
