package com.olsplus.balancemall.app.login.bussiness;


import android.content.Context;
import android.text.TextUtils;

import com.olsplus.balancemall.app.login.bean.LoginResultEntity;
import com.olsplus.balancemall.app.login.bean.RegisterResultEntity;
import com.olsplus.balancemall.app.login.request.ILoginRequest;
import com.olsplus.balancemall.app.login.request.LoginService;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.FinalHttpResultObserver;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.EncrypUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.UrlConst;

import java.util.HashMap;
import java.util.Map;

import retrofit2.http.HTTP;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginBusiness {

    private Context context;

    public LoginBusiness(Context context) {
        this.context = context;
    }

    /**
     * 登录请求
     *
     * @param phone
     * @param pwd
     * @param callback
     */
    public void login(final String phone, final String pwd, final ILoginRequest.LoginCallback callback) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/login";
        String versionCode = String.valueOf(AppUtil.getVersionName(context));
        String password = EncrypUtil.eccryptSHA1(pwd);
        String sign = parseLoginSign(url, phone, password, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<LoginResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                login(phone, pwd, callback);
            }

            @Override
            public void handleSuccessResp(LoginResultEntity data) {
                String uid = data.getUid();
                String token = data.getToken();
                String avatar = data.getAvatar();
                SPUtil.put(context, SPUtil.UID, uid);
                SPUtil.put(context, SPUtil.TOKEN, token);
                SPUtil.put(context, SPUtil.PHONE, data.getPhone());
                if (!TextUtils.isEmpty(avatar)) {
                    SPUtil.put(context, SPUtil.AVATAR, data.getAvatar());
                }
                SPUtil.put(context, SPUtil.BUILDING_ID, data.getBuilding_id());
                if (!TextUtils.isEmpty(data.getBuilding_name())) {
                    SPUtil.put(context, SPUtil.BUILDING_NAME, data.getBuilding_name());
                } else {
                    SPUtil.put(context, SPUtil.BUILDING_NAME, "");
                }
                SPUtil.put(context, SPUtil.POINTS, data.getPoints());
                callback.onLoginSuccess(data);
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("login", "onLoginFailed");
                callback.onLoginFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(LoginService.class)
                .login(phone, password, HttpUtil.PLATFORM, HttpUtil.CHANNEL, versionCode, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    public void register(final String phone, final String name, final String pwd, final ILoginRequest.RegisterCallback callback) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/register";
        String versionCode = String.valueOf(AppUtil.getVersionName(context));
        String password = EncrypUtil.eccryptSHA1(pwd);
        String sign = parseRegisterSign(url, phone, name, password, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<RegisterResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                register(phone, name, pwd, callback);
            }

            @Override
            public void handleSuccessResp(RegisterResultEntity data) {
                String uid = data.getUid();
                String token = data.getToken();
                int points = data.getPoints();
                SPUtil.put(context, SPUtil.UID, uid);
                SPUtil.put(context, SPUtil.TOKEN, token);
                SPUtil.put(context, SPUtil.POINTS, points);
                callback.onRegisterSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("login", "onLoginFailed");
                callback.onRegisterFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(LoginService.class)
                .register(phone, name, password, HttpUtil.PLATFORM, HttpUtil.CHANNEL, versionCode, ApiConst.REGISTER_TYPE, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 发送注册的短信请求
     *
     * @param phone
     * @param callback
     */
    public void sendSms(final String phone, final ILoginRequest.SendsmsCallback callback) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/register/sms_send";
        String sign = parseSendsmsSign(url, phone, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                sendSms(phone, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onSendSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan.w", "sendSms");
                callback.onSendFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(LoginService.class)
                .sendSms(phone, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }


    /**
     * 发送忘记密码的短信接口
     *
     * @param phone
     * @param callback
     */
    public void sendForgetPwdSms(final String phone, final ILoginRequest.SendsmsCallback callback) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/changepwd/sms_send";
        String sign = parseSendsmsSign(url, phone, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                sendForgetPwdSms(phone, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onSendSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan.w", "sendPwdSms");
                callback.onSendFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(LoginService.class)
                .sendPwdSms(phone, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 忘记密码时，修改密码
     *
     * @param phone
     * @param pwd
     * @param callback
     */
    public void forgetPwd(final String phone, final String pwd, final ILoginRequest.ForgetPwdCallback callback) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/changepwd";
        String password = EncrypUtil.eccryptSHA1(pwd);
        String sign = parseForgetPwdSign(url, phone, password, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                forgetPwd(phone, pwd, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onShowForgetPwdSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan.w", "forgetPwd");
                callback.onShowForgetPwdFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(LoginService.class)
                .forgetPwd(password, phone, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    public void loginOut(final ILoginRequest.LoginOutCallback callback) {

        String url = ApiConst.BASE_URL + "v1/logout";
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String versionCode = String.valueOf(AppUtil.getVersionName(context));
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String sign = parseLoginOutSign(url, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                loginOut(callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onLoginOutSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("login", "loginOut");
                callback.onLoginOutFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(LoginService.class)
                .loginOut(uid, token, HttpUtil.PLATFORM, HttpUtil.CHANNEL, versionCode, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 检验短信验证码
     *
     * @param phone
     * @param sms
     * @param type
     * @param callback
     */
    public void checkSms(String phone, String sms, String type, final RequestCallback<BaseResultEntity> callback) {

        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("phone", phone);
        paramMap.put("sms", sms);
        paramMap.put("type", type);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.login_check_sms, paramMap);

        HttpManager.getRetrofit()
                .create(LoginService.class)
                .checkSms(phone, sms, type, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FinalHttpResultObserver<>(callback));
    }


    /**
     * 生成登录签名
     *
     * @param url
     * @param phone
     * @param pwd
     * @return
     */
    private String parseLoginSign(String url, String phone, String pwd, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("phone", phone);
        paramMap.put("pwd", pwd);
        paramMap.put("channel", HttpUtil.CHANNEL);
        paramMap.put("version", String.valueOf(AppUtil.getVersionName(context)));
        paramMap.put("platform", HttpUtil.PLATFORM);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    /**
     * 生成注册签名
     *
     * @param url
     * @param phone
     * @param name
     * @param pwd
     * @return
     */
    private String parseRegisterSign(String url, String phone, String name, String pwd, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("phone", phone);
        paramMap.put("name", name);
        paramMap.put("pwd", pwd);
        paramMap.put("channel", HttpUtil.CHANNEL);
        paramMap.put("version", String.valueOf(AppUtil.getVersionName(context)));
        paramMap.put("platform", HttpUtil.PLATFORM);
        paramMap.put("register_type", "1");
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseSendsmsSign(String url, String phone, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("phone", phone);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseForgetPwdSign(String url, String phone, String pwd, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("phone", phone);
        paramMap.put("newpwd", pwd);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseLoginOutSign(String url, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("channel", HttpUtil.CHANNEL);
        paramMap.put("version", String.valueOf(AppUtil.getVersionName(context)));
        paramMap.put("platform", HttpUtil.PLATFORM);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

}
