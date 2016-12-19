package com.olsplus.balancemall.app.pay.business;


import android.content.Context;

import com.olsplus.balancemall.app.pay.bean.ShopingOrderSubmitResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingPayResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingResultByZero;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingVoucherResultEntity;
import com.olsplus.balancemall.app.pay.bean.ShoppingWxPayResult;
import com.olsplus.balancemall.app.pay.request.ICheckOutRequest;
import com.olsplus.balancemall.app.pay.request.OrderService;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckOutBussiness {

    private Context context;

    public CheckOutBussiness(Context context) {
        this.context = context;
    }

    /**
     * 提交订单
     *
     * @param order
     * @param callback
     */
    public void sumitOrder(final String order, final ICheckOutRequest.SumitOrderCallback callback) {
        String url = ApiConst.BASE_URL + "v1/order";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseSumitOrderSign(url, uid, token, order, timestamp);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), order);
        String requestUrl = "v1/order?uid=" + uid + "&token=" + token + "&timestamp=" + timestamp + "&sign=" + sign;
        HttpResultObserver respObserver = new HttpResultObserver<ShopingOrderSubmitResultEntity>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                sumitOrder(order, callback);
            }

            @Override
            public void onSuccess(ShopingOrderSubmitResultEntity data) {
                if (data == null) {
                    callback.onSumitOrderFailed("数据出错了");
                    return;
                }
//                String pointRule = String.valueOf(data.getPoint_rule());
//                SPUtil.put(context,SPUtil.POINT_RULE,pointRule);
                ApiConst.POINT_RULE = data.getPoint_rule();
                callback.onSumitOrderSuccess(data);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("yongyuan,w", "sumitOrder failed");
                callback.onSumitOrderFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(OrderService.class)
                .sumitOrder(requestUrl, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 支付宝订单支付
     *
     * @param payRequest
     * @param callback
     */
    public void startAliBuyStep(final String payRequest, final ICheckOutRequest.PayCallback callback) {
        String url = ApiConst.BASE_URL + "v1/payment/prepare";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseSumitOrderSign(url, uid, token, payRequest, timestamp);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), payRequest);
        String requestUrl = "v1/payment/prepare?uid=" + uid + "&token=" + token + "&timestamp=" + timestamp + "&sign=" + sign;
        HttpResultObserver respObserver = new HttpResultObserver<ShoppingPayResultEntity>() {

            @Override
            public void onPrepare() {
            }

            @Override
            public void onReconnect() {
                startAliBuyStep(payRequest, callback);
            }

            @Override
            public void onSuccess(ShoppingPayResultEntity data) {
                if (data == null) {
                    callback.onPayFailed("数据出错了");
                    return;
                }
                callback.onPaySuccess(data);
            }

            @Override
            public void onFail(String msg) {
                callback.onPayFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(OrderService.class)
                .prepareAliPay(requestUrl, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 微信支付
     *
     * @param payRequest
     * @param callback
     */
    public void startWxBuyStep(final String payRequest, final ICheckOutRequest.WxPayCallback callback) {
        String url = ApiConst.BASE_URL + "v1/payment/prepare";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseSumitOrderSign(url, uid, token, payRequest, timestamp);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), payRequest);
        String requestUrl = "v1/payment/prepare?uid=" + uid + "&token=" + token + "&timestamp=" + timestamp + "&sign=" + sign;
        HttpResultObserver respObserver = new HttpResultObserver<ShoppingWxPayResult>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                startWxBuyStep(payRequest, callback);
            }

            @Override
            public void onSuccess(ShoppingWxPayResult data) {
                if (data == null) {
                    callback.onPayFailed("数据出错了");
                    return;
                }
                callback.onPaySuccess(data);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("yongyuan,w", "startBuyStep failed");
                callback.onPayFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(OrderService.class)
                .prepareWxPay(requestUrl, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    public void startBuyStep(final String payRequest, final ICheckOutRequest.SpecialPayCallback callback) {
        String url = ApiConst.BASE_URL + "v1/payment/onPrepare";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseSumitOrderSign(url, uid, token, payRequest, timestamp);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), payRequest);
        String requestUrl = "v1/payment/onPrepare?uid=" + uid + "&token=" + token + "&timestamp=" + timestamp + "&sign=" + sign;
        HttpResultObserver respObserver = new HttpResultObserver<ShoppingResultByZero>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                startBuyStep(payRequest, callback);
            }

            @Override
            public void onSuccess(ShoppingResultByZero data) {
                if (data == null) {
                    callback.onPayFailed("数据出错了");
                    return;
                }
                callback.onPaySuccess(data);
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("yongyuan,w", "preparePay failed");
                callback.onPayFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(OrderService.class)
                .preparePay(requestUrl, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 获取票券列表
     *
     * @param callback
     */
    public void getVouchers(final ICheckOutRequest.GetVoucherCallback callback) {
        String url = ApiConst.BASE_URL + "v1/voucher";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseVoucherSign(url, uid, token, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<ShoppingVoucherResultEntity>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                getVouchers(callback);
            }

            @Override
            public void onSuccess(ShoppingVoucherResultEntity data) {
                if (data == null) {
                    callback.onVoucherFailed("数据出错了");
                    return;
                }
                List<ShoppingVoucherEntity> shoppingVoucherEntityList = data.getVouchers();
                if (shoppingVoucherEntityList != null && !shoppingVoucherEntityList.isEmpty()) {
                    callback.onVoucherSuccess(shoppingVoucherEntityList);
                } else {
                    callback.onVoucherFailed("没有优惠券");
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("yongyuan.w", "getVouchers failed");
                callback.onVoucherFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(OrderService.class)
                .getVouchers(uid, token, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 生成提交订单签名
     *
     * @param url
     * @param uid
     * @param token
     * @param order
     * @param timestamp
     * @return
     */
    private String parseSumitOrderSign(String url, String uid, String token, String order, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.signWithJson(HttpUtil.POST, url, paramMap, order);
        return sign;
    }

    /**
     * 生成获取票券列表签名
     *
     * @param url
     * @param uid
     * @param token
     * @return
     */
    private String parseVoucherSign(String url, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }
}
