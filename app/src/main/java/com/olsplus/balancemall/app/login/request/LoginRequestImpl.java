package com.olsplus.balancemall.app.login.request;


import android.content.Context;

import com.olsplus.balancemall.app.login.bean.LoginResultEntity;
import com.olsplus.balancemall.app.login.bussiness.LoginBusiness;
import com.olsplus.balancemall.app.login.view.IForgetPwdView;
import com.olsplus.balancemall.app.login.view.ILoginView;
import com.olsplus.balancemall.app.login.view.IRegisterView;
import com.olsplus.balancemall.app.login.view.IloginOutView;

public class LoginRequestImpl implements ILoginRequest{

    private Context context;

    private LoginBusiness loginBusiness;
    private ILoginView iLoginView;
    private IRegisterView iRegisterView;
    private IForgetPwdView iForgetPwdView;
    private IloginOutView iloginOutView;

    public LoginRequestImpl(Context context) {
        loginBusiness = new LoginBusiness(context);
    }

    public void setLoginView(ILoginView view){
        this.iLoginView = view;
    }

    public void setRegisterView(IRegisterView view){
        this.iRegisterView = view;
    }

    public void setiForgetPwdView(IForgetPwdView iForgetPwdView) {
        this.iForgetPwdView = iForgetPwdView;
    }

    public void setIloginOutView(IloginOutView iloginOutView) {
        this.iloginOutView = iloginOutView;
    }

    @Override
    public void login(String name, String password) {
        loginBusiness.login(name, password, new LoginCallback() {
            @Override
            public void onLoginSuccess(LoginResultEntity data) {
                if(iLoginView!=null){
                    iLoginView.onLoginSuccess(data);
                }
            }

            @Override
            public void onLoginFailed(String msg) {
                if(iLoginView!=null){
                    iLoginView.showLoginError(msg);
                }
            }
        });
    }

    @Override
    public void register(String phone, String name, String password) {
        loginBusiness.register(phone, name, password, new RegisterCallback() {
            @Override
            public void onRegisterSuccess() {
                if(iRegisterView!=null){
                    iRegisterView.onRegisterSuccess();
                }
            }

            @Override
            public void onRegisterFailed(String msg) {
                if(iRegisterView!=null){
                    iRegisterView.showRegisterError(msg);
                }
            }
        });
    }

    @Override
    public void sendSms(String phone) {
        loginBusiness.sendSms(phone, new SendsmsCallback() {
            @Override
            public void onSendSuccess() {
                if(iRegisterView!=null){
                    iRegisterView.showSendsmsSuccess();
                }
            }

            @Override
            public void onSendFailed(String msg) {
                if(iRegisterView!=null){
                    iRegisterView.showSendsmsFailedView(msg);
                }
            }
        }) ;
    }

    @Override
    public void sendForgetPwdSms(String phone) {
        loginBusiness.sendForgetPwdSms(phone, new SendsmsCallback() {
            @Override
            public void onSendSuccess() {
                if(iForgetPwdView!=null){
                    iForgetPwdView.showSendsmsSuccess();
                }
            }

            @Override
            public void onSendFailed(String msg) {
                if(iForgetPwdView!=null){
                    iForgetPwdView.showSendsmsFailedView(msg);
                }
            }
        }); ;
    }

    @Override
    public void forgetPwd(String phone, String pwd) {
        loginBusiness.forgetPwd(phone, pwd,new ForgetPwdCallback() {
            @Override
            public void onShowForgetPwdSuccess() {
                if(iForgetPwdView!=null){
                    iForgetPwdView.showForgetPwdSuccess();
                }
            }

            @Override
            public void onShowForgetPwdFailed(String msg) {
                if(iForgetPwdView!=null){
                    iForgetPwdView.showForgetPwdFailedView(msg);
                }
            }
        }); ;
    }

    @Override
    public void loginOut() {
        loginBusiness.loginOut(new LoginOutCallback() {
            @Override
            public void onLoginOutSuccess() {
                if(iloginOutView!=null){
                    iloginOutView.onLogoutSuccess();
                }
            }

            @Override
            public void onLoginOutFailed(String msg) {
                if(iloginOutView!=null){
                    iloginOutView.showLogoutError(msg);
                }
            }
        });
    }
}
