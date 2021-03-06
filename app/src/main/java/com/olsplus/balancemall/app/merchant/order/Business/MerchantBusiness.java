package com.olsplus.balancemall.app.merchant.order.Business;


import android.content.Context;

import com.olsplus.balancemall.app.merchant.order.bean.MerchantEntity;
import com.olsplus.balancemall.app.merchant.order.request.MerchantService;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.DoTransform;
import com.olsplus.balancemall.core.http.FinalHttpResultObserver;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.http.RequestCallback;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.UrlConst;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MerchantBusiness {

    private static MerchantService service = HttpManager.getRetrofit().create(MerchantService.class);

    /**
     * 获取商家首页
     *
     * @return
     */
    public static Observable<MerchantEntity> getMerchant() {

        String uid = HttpUtil.getUid();
        String token = HttpUtil.getToken();
        String timestamp = HttpUtil.getTimestamp();
        String local_service_id = HttpUtil.getLocalServiceId();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, UrlConst.merchant.merchant_home, paramMap);

        return service.getMerchant(uid, token, local_service_id, timestamp, sign)
                .compose(DoTransform.<MerchantEntity>applyScheduler());
    }

    public static Observable<MerchantEntity> getdadfdsfa(final Context context) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, UrlConst.merchant.merchant_home, paramMap);

        return service.getMerchant(uid, token, local_service_id, timestamp, sign)
                .compose(DoTransform.<MerchantEntity>applyScheduler());
    }


    /**
     * 获取商家订单列表
     *
     * @param context
     * @param page
     * @param count
     * @param local_service_id
     * @param orderType
     */
    public static void getOrderList(Context context, int page, int count, String local_service_id, String orderType, final RequestCallback<BaseResultEntity> callback) {

        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("page", String.valueOf(page));
        paramMap.put("count", String.valueOf(count));
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("orderType", orderType);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, UrlConst.merchant.merchant_order_list, paramMap);

        service.getMerchantOrder(uid, token, local_service_id, orderType, timestamp, page, count, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FinalHttpResultObserver<>(callback));
    }

    /**
     * 获取订单详情
     *
     * @param context
     * @param order_id
     * @param callback
     */
    public static void getMerchantOrderDetail(Context context, long order_id, final RequestCallback<BaseResultEntity> callback) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("order_id", String.valueOf(order_id));
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, UrlConst.merchant.merchant_order_detail, paramMap);

        service.getMerchantOrderDetail(uid, token, timestamp, sign, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FinalHttpResultObserver<>(callback));
    }

    /**
     * 同意退款
     *
     * @param context
     * @param order_id
     * @param product_id
     * @param callback
     */
    public static void agreeRefund(Context context, long order_id, long product_id, final RequestCallback<BaseResultEntity> callback) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("order_id", String.valueOf(order_id));
        paramMap.put("product_id", String.valueOf(product_id));
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.merchant_agree_refund, paramMap);

        service.agreeRefund(uid, token, timestamp, sign, order_id, product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FinalHttpResultObserver<>(callback));
    }

    /**
     * 拒绝退款
     *
     * @param context
     * @param order_id
     * @param product_id
     * @param callback
     */
    public static void refuseRefund(Context context, long order_id, long product_id, final RequestCallback<BaseResultEntity> callback) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("order_id", String.valueOf(order_id));
        paramMap.put("product_id", String.valueOf(product_id));
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.merchant_refuse_refund, paramMap);

        service.refuseRefund(uid, token, timestamp, sign, order_id, product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FinalHttpResultObserver<>(callback));
    }

    /**
     * 完成订单
     *
     * @param context
     * @param order_id
     * @param product_id
     * @param callback
     */
    public static void finishMerchantOrder(Context context, long order_id, long product_id, final RequestCallback<BaseResultEntity> callback) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("order_id", String.valueOf(order_id));
        paramMap.put("product_id", String.valueOf(product_id));
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.merchant_finish_order, paramMap);

        service.finishOrder(uid, token, timestamp, sign, order_id, product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FinalHttpResultObserver<>(callback));
    }

    /**
     * 取消订单
     *
     * @param context
     * @param order_id
     * @param callback
     */
    public static void cancelMerchantOrder(Context context, long order_id, final RequestCallback<BaseResultEntity> callback) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("order_id", String.valueOf(order_id));
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.merchant_cancel_order, paramMap);

        service.cancelMerchantOrder(uid, token, timestamp, sign, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FinalHttpResultObserver<>(callback));
    }
}
