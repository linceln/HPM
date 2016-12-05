package com.olsplus.balancemall.app.cart.bussiness;


import android.content.Context;
import android.text.TextUtils;

import com.olsplus.balancemall.app.cart.bean.CartResultEntity;
import com.olsplus.balancemall.app.cart.bean.DeleteCartReusltEntity;
import com.olsplus.balancemall.app.cart.bean.ShoppingBag;
import com.olsplus.balancemall.app.cart.request.CartService;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CartBussiness {

    private Context context;

    public CartBussiness(Context context) {
        this.context = context;
    }

    public void addCart(final String local_service_id, final String product_id, final String count, final String price,final String sku_id, final String sku_value,final String schedule_time, final OnAddCartCallback callback) {
        String url = ApiConst.BASE_URL + "v1/cart/add";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseAddCartSign(url, timestamp, local_service_id, product_id, count, price, sku_id, sku_value, schedule_time);
        HttpResultObserver respObserver = new HttpResultObserver<CartResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                addCart(local_service_id, product_id, count, price, sku_id, sku_value, schedule_time, callback);
            }

            @Override
            public void handleSuccessResp(CartResultEntity data) {
                if (data == null) {
                    callback.addCartFailed("数据出错了");
                    return;
                }
                callback.addCartSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan.w", "addcart failed");
                callback.addCartFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(CartService.class)
                .addCart(uid, token, timestamp, sign, local_service_id, product_id, count, price, sku_id, sku_value, schedule_time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    public void deleteCart(final String json,final  OnDeleteCartCallback callback){
        String url = ApiConst.BASE_URL + "v1/cart/delete";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseDeleteCartSign(url, timestamp,uid, token,json);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        String requestUrl = "v1/cart/delete?uid="+uid+"&token="+token+"&timestamp="+timestamp+"&sign="+sign;
        HttpResultObserver respObserver = new HttpResultObserver<DeleteCartReusltEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                deleteCart(json, callback);
            }

            @Override
            public void handleSuccessResp(DeleteCartReusltEntity data) {
                if (data == null) {
                    callback.deleteCartFailed("数据出错了");
                    return;
                }
                callback.deleteCartSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan.w", "delete cart failed");
                callback.deleteCartFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(CartService.class)
                .deleteCart(requestUrl,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 获取购物车列表
     * @param page
     * @param count
     * @param callback
     */
    public void getCartListData(final String page,final String count,final OnGetCartDataCallback callback ){
        String url = ApiConst.BASE_URL + "v1/cart";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseGetCartListSign(url, uid, token,timestamp,page,count);
        HttpResultObserver respObserver = new HttpResultObserver<ShoppingBag>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                getCartListData(page, count, callback);
            }

            @Override
            public void handleSuccessResp(ShoppingBag data) {
                if (data == null) {
                    callback.getCartFailed("数据出错了");
                    return;
                }
                if (data.getCart() == null) {
                    callback.getCartFailed("数据出错了");
                    return;
                }
                callback.getCartSuccess(data.getCart());
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "getCartListData failed");
                callback.getCartFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(CartService.class)
                .getCartListData(uid,token,timestamp,sign,page,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }


    /**
     * 生成添加购物车签名
     *
     * @param url
     * @param timestamp
     * @param local_service_id
     * @param product_id
     * @param count
     * @param price
     * @param sku_id
     * @param sku_value
     * @param schedule_time
     * @return
     */
    private String parseAddCartSign(String url, String timestamp, String local_service_id, String product_id, String count, String price, String sku_id, String sku_value, String schedule_time) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("product_id", product_id);
        paramMap.put("count", count);
        paramMap.put("price", price);
        if (!TextUtils.isEmpty(sku_id)) {
            paramMap.put("sku_id", sku_id);
        }
        if (!TextUtils.isEmpty(sku_value)) {
            paramMap.put("sku_value", sku_value);
        }
        if (!TextUtils.isEmpty(schedule_time)) {
            paramMap.put("schedule_time", schedule_time);
        }
        paramMap.put("uid", (String) SPUtil.get(context, SPUtil.UID, ""));
        paramMap.put("timestamp", timestamp);
        paramMap.put("token", (String) SPUtil.get(context, SPUtil.TOKEN, ""));
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    /**
     * 生成购物车签名
     * @param url
     * @param timestamp
     * @param uid
     * @param token
     * @param json
     * @return
     */
    private String parseDeleteCartSign(String url, String timestamp,String uid,String token,String json){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.signWithJson(HttpUtil.POST, url, paramMap,json);
        return sign;
    }

    private String parseGetCartListSign(String url, String uid, String token,String timestamp,String page,String count){
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("page", page);
        paramMap.put("count", count);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

}
