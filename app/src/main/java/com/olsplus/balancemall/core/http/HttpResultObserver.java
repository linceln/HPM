package com.olsplus.balancemall.core.http;


import android.text.TextUtils;

import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.token.TokenManager;
import com.olsplus.balancemall.core.util.LogUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

public abstract class HttpResultObserver<T extends BaseResultEntity> extends Subscriber<T> {

    private static final String TAG = "HttpResultObserver";

    public abstract void onSuccess(T entity);

    public abstract void onFail(String msg);

    public void onPrepare() {
    }


    public void onReconnect() {
    }


    @Override
    public void onStart() {
        super.onStart();
        onPrepare();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            onFail("网络中断，请检查您的网络状态");
        } else {
            onFail("网络错误，请重试");
            if (e.getCause() != null) {
//                onFail(e.getCause().getMessage());
                LogUtil.e(TAG, e.getCause().getMessage());
            } else {
//                onFail(e.getMessage());
                LogUtil.e(TAG, e.getMessage());
            }
        }
    }


    @Override
    public void onNext(T t) {
        if (t.getRet() == 0) {
            onSuccess(t);
        } else {
            if (t.getRet() == 3101 || t.getRet() == 3100) {
                // token过期，刷新token
                TokenManager.requestUpdateToken(this);
            } else {
                String msg = getErrorMsg(t.getRet());
                if (TextUtils.isEmpty(msg)) {
                    msg = t.getMsg();
                }
                LogUtil.e(TAG, msg);
                onFail(msg);
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
}
