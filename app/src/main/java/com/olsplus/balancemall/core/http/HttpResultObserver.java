package com.olsplus.balancemall.core.http;


import android.text.TextUtils;

import com.olsplus.balancemall.core.app.MyApplication;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.bean.TokenResult;
import com.olsplus.balancemall.core.event.TokenEvent;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class HttpResultObserver<T extends BaseResultEntity> extends Subscriber<T> {


    public abstract void handleSuccessResp(T data);

    public abstract void handleFailedResp(String msg);

    public abstract void prepare();

    public abstract void reConnect();


    @Override
    public void onStart() {
        super.onStart();
        prepare();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            handleFailedResp("网络中断，请检查您的网络状态");
        } else {
            if (e.getCause() != null) {
                handleFailedResp(e.getCause().getMessage());
            }else{
                handleFailedResp(e.getMessage());
            }
        }
    }


    @Override
    public void onNext(T t) {
        if (t.getRet() == 0) {
            handleSuccessResp(t);
        } else {
            if (t.getRet() == 3101 || t.getRet() == 3100) {
//                updateToken();
                requestUpdateToken();
            } else {
                String msg = getErrorMsg(t.getRet());
                if (TextUtils.isEmpty(msg)) {
                    msg = t.getMsg();
                }
                handleFailedResp(msg);
            }
        }
    }

    private String getErrorMsg(int ret) {
        String msg = "";
        switch (ret) {
            case 1000:
                msg = "网络连接异常";
                break;
            case 1100:
                msg = "服务器繁忙";
                break;
            case 1200:
                msg = "服务器数据异常";
                break;
            case 1300:
                msg = "未知异常";
                break;
            case 1400:
                msg = "接口不存在";
                break;
            case 1500:
                msg = "记录不存在";
                break;
            case 1600:
                msg = "上传失败";
                break;
            case 2000:
                msg = "签名校验出错,操作无效";
                break;
            case 2001:
                msg = "调用超时";
                break;
            case 2100:
                msg = "接口操作无效";
                break;
            case 2200:
                msg = "参数格式错误";
                break;
            case 2300:
                msg = "访问频率超限访问太快，休息一会吧";
                break;
            case 3000:
                msg = "手机号或密码错误";
                break;
            case 3001:
                msg = "手机号已注册";
                break;
            case 3002:
                msg = "昵称已被占用";
                break;
            case 3200:
                msg = "预约时间失效，请重新预约！";
                break;
            case 3300:
                msg = "支付订单数据错误";
                break;
            case 3301:
                msg = "已有支付订单处理中,请勿重复支付!";
                break;
            case 3310:
                msg = "支付失败";
                break;
            case 3311:
                msg = "交易关闭";
                break;
            case 3320:
                msg = "微信预支付失败";
                break;
        }
        return msg;
    }

    private void requestUpdateToken() {
        final UpdateTokenManager updateTokenManager = UpdateTokenManager.getInstance();
        if (updateTokenManager != null) {
            if (updateTokenManager.isHasTask()) {
                UpdateTokenTask updateTokenTask = new UpdateTokenTask();
                updateTokenManager.addTask(updateTokenTask);
                updateTokenTask.updateToken(new UpdateCallBack() {
                    @Override
                    public void updateSuccess() {
                        updateTokenManager.removeTask();
                        reConnect();
                    }

                    @Override
                    public void updateFailed() {
                        updateTokenManager.removeTask();
                        ToastUtil.showShort(MyApplication.getApp(), "刷新token失败");
                        EventBus.getDefault().post(new TokenEvent());
                    }
                });
            }
        }
    }


    public void updateToken() {
        String url = ApiConst.BASE_URL + "v1/token";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(MyApplication.getApp(), SPUtil.UID, "");
        String token = (String) SPUtil.get(MyApplication.getApp(), SPUtil.TOKEN, "");
        LogUtil.d("yongyuan.w", "token2=" + token);
        String sign = parseUpdateTokenSign(url, uid, token, timestamp);
        HttpManager.getRetrofit()
                .create(TokenService.class)
                .getToken(uid, token, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TokenResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TokenResult tokenResult) {
                        if (tokenResult == null) {
                            return;
                        }
                        if (tokenResult.getRet() == 0) {
                            String token = tokenResult.getToken();
                            SPUtil.put(MyApplication.getApp(), SPUtil.TOKEN, token);
                            reConnect();
                        } else {
                            ToastUtil.showShort(MyApplication.getApp(), "刷新token失败");
                            EventBus.getDefault().post(new TokenEvent());
                        }

                    }
                });
    }

    private String parseUpdateTokenSign(String url, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }
}
