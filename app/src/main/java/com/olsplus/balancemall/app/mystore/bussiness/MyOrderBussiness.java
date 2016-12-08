package com.olsplus.balancemall.app.mystore.bussiness;


import android.content.Context;
import android.text.TextUtils;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.bean.ImageUploadEntity;
import com.olsplus.balancemall.app.mystore.bean.MyOrderDetailResult;
import com.olsplus.balancemall.app.mystore.bean.MyOrderResult;
import com.olsplus.balancemall.app.mystore.bean.ReturnImgResult;
import com.olsplus.balancemall.app.mystore.request.MyOrderService;
import com.olsplus.balancemall.app.mystore.request.IMyOrderRequest;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.UrlConst;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MyOrderBussiness {

    private Context context;

    public MyOrderBussiness(Context context) {
        this.context = context;
    }


    /**
     * 获取订单列表
     *
     * @param orderType
     * @param page
     * @param count
     * @param callback
     */
    public void getMyOrderListData(final String orderType, final String page, final String count, final IMyOrderRequest.GetMyOrderListCallback callback) {
        String url = ApiConst.BASE_URL + "v1/order/list";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseGetMyOrderListSign(url, uid, token, timestamp, orderType, page, count);
        HttpResultObserver respObserver = new HttpResultObserver<MyOrderResult>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                getMyOrderListData(orderType, page, count, callback);
            }

            @Override
            public void handleSuccessResp(MyOrderResult data) {
                if (data == null) {
                    callback.onGetOrderListFailed("数据出错了");
                    return;
                }
                if (data.getOrders() == null) {
                    callback.onGetOrderListFailed("数据出错了");
                    return;
                }
                callback.onOrderListSuccess(data);
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "getMyOrderListData failed");
                callback.onGetOrderListFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MyOrderService.class)
                .getMyOrderListData(uid, token, timestamp, sign, orderType, page, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 取消订单
     *
     * @param order_id
     * @param callback
     */
    public void cancelOrder(final String order_id, final IMyOrderRequest.OnOrderCancelCallback callback) {
        String url = ApiConst.BASE_URL + "v1/order/cancel";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseCancelOrderSign(url, uid, token, timestamp, order_id);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                cancelOrder(order_id, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "cancelOrder failed");
                callback.onFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MyOrderService.class)
                .cancelOrder(uid, token, timestamp, sign, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 删除订单
     *
     * @param order_id
     * @param callback
     */
    public void deleteOrder(final String order_id, final IMyOrderRequest.OnOrderDeleteCallback callback) {
        String url = ApiConst.BASE_URL + "v1/order/delete";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseCancelOrderSign(url, uid, token, timestamp, order_id);
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                deleteOrder(order_id, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "deleteOrder failed");
                callback.onFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MyOrderService.class)
                .deleteOrder(uid, token, timestamp, sign, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }


    /**
     * 获取订单详情
     *
     * @param orderId
     * @param callback
     */
    public void getMyOrderDetailData(final String orderId, final IMyOrderRequest.GetMyOrderDetailCallback callback) {
        String url = ApiConst.BASE_URL + "v1/order/detail";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseGetMyOrderDetailSign(url, uid, token, timestamp, orderId);
        HttpResultObserver respObserver = new HttpResultObserver<MyOrderDetailResult>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                getMyOrderDetailData(orderId, callback);
            }

            @Override
            public void handleSuccessResp(MyOrderDetailResult data) {
                if (data == null) {
                    callback.onGetOrderDetailFailed("数据出错了");
                    return;
                }
                if (data.getOrder_detail() == null) {
                    callback.onGetOrderDetailFailed("数据出错了");
                    return;
                }
                callback.onOrderDetailSuccess(data);
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "getMyOrderDetailData failed");
                callback.onGetOrderDetailFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MyOrderService.class)
                .getMyOrderDetailData(uid, token, timestamp, sign, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 得到上传图片所需的token和文件名
     *
     * @param img
     */
    public Observable<ImageUploadEntity> getProofToken(final String img) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("img", img);
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.customer.customer_order_proof_upload, paramMap);

        return HttpManager.getRetrofit()
                .create(MyOrderService.class)
                .getProofToken(uid, token, sign, timestamp, img)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ImageUploadEntity, Observable<ImageUploadEntity>>() { // 嵌套调用错误处理
                    @Override
                    public Observable<ImageUploadEntity> call(ImageUploadEntity imageUploadEntity) {
                        if (imageUploadEntity.getRet() == 0) {
                            imageUploadEntity.setCompressedPath(img);
                            return Observable.just(imageUploadEntity);
                        } else {
                            return Observable.error(new Throwable(imageUploadEntity.getMsg()));
                        }
                    }
                });
    }

//    public void uploadReturnImg(final String picPath, final IMyOrderRequest.UpoadReturnImgCallback callback) {
//        if (TextUtils.isEmpty(picPath)) {
//            return;
//        }
//        File file = new File(picPath);
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
////        MultipartBody.Part body =
////                MultipartBody.Part.createFormData("img", file.getName(), requestFile);
//        String url = ApiConst.BASE_URL + "v1/upload/refund_img";
//        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
//        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
//        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
//        String sign = parseUploadReturnImgSign(url, uid, token, timestamp);
//        HttpResultObserver respObserver = new HttpResultObserver<ReturnImgResult>() {
//
//            @Override
//            public void prepare() {
//
//            }
//
//            @Override
//            public void reConnect() {
//                uploadReturnImg(picPath, callback);
//            }
//
//            @Override
//            public void handleSuccessResp(ReturnImgResult data) {
//                if (data == null) {
//                    callback.onUploadFailed("数据出错了");
//                    return;
//                }
//                callback.onUploadSuccess(data.getImg());
//            }
//
//            @Override
//            public void handleFailedResp(String msg) {
//                LogUtil.d("yongyuan,w", "uploadReturnImg failed");
//                callback.onUploadFailed(msg);
//            }
//        };
//        HttpManager.getRetrofit()
//                .create(MyOrderService.class)
//                .uploadReturnImg(uid, token, timestamp, sign, requestFile)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(respObserver);
//    }

    /**
     * 申请退款
     *
     * @param json
     * @param callback
     */
    public void requestReturnOrder(final String json, final IMyOrderRequest.OnReturnOrderCallback callback) {
        String url = ApiConst.BASE_URL + "v1/refund/request";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseReturnOrderSign(url, uid, token, json, timestamp);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        String requestUrl = "v1/refund/request?uid=" + uid + "&token=" + token + "&timestamp=" + timestamp + "&sign=" + sign;
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                requestReturnOrder(json, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onRequestSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "requestReturnOrder failed");
                callback.onRequestFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MyOrderService.class)
                .requestReturnOrder(requestUrl, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 提交评价
     *
     * @param json
     * @param callback
     */
    public void sumitComment(final String json, final IMyOrderRequest.OnSumitCommentCallback callback) {
        String url = ApiConst.BASE_URL + "v1/order/remark";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseReturnOrderSign(url, uid, token, json, timestamp);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        String requestUrl = "v1/order/remark?uid=" + uid + "&token=" + token + "&timestamp=" + timestamp + "&sign=" + sign;
        HttpResultObserver respObserver = new HttpResultObserver<BaseResultEntity>() {

            @Override
            public void prepare() {

            }

            @Override
            public void reConnect() {
                sumitComment(json, callback);
            }

            @Override
            public void handleSuccessResp(BaseResultEntity data) {
                callback.onSumitSuccess();
            }

            @Override
            public void handleFailedResp(String msg) {
                LogUtil.d("yongyuan,w", "sumitComment failed");
                callback.onSumitFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(MyOrderService.class)
                .sumitComment(requestUrl, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 生成订单列表签名
     *
     * @param url
     * @param uid
     * @param token
     * @param timestamp
     * @param orderType
     * @param page
     * @param count
     * @return
     */
    private String parseGetMyOrderListSign(String url, String uid, String token, String timestamp, String orderType, String page, String count) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("orderType", orderType);
        paramMap.put("page", page);
        paramMap.put("count", count);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    private String parseGetMyOrderDetailSign(String url, String uid, String token, String timestamp, String order_id) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("order_id", order_id);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    private String parseCancelOrderSign(String url, String uid, String token, String timestamp, String order_id) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("order_id", order_id);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

    private String parseReturnOrderSign(String url, String uid, String token, String body, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.signWithJson(HttpUtil.POST, url, paramMap, body);
        return sign;
    }

    private String parseUploadReturnImgSign(String url, String uid, String token, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }

}
