package com.olsplus.balancemall.app.merchant.goods.business;


import android.content.Context;

import com.google.gson.Gson;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.goods.bean.AddResultEntity;
import com.olsplus.balancemall.app.merchant.goods.bean.EditGoodsEntity;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsDetail;
import com.olsplus.balancemall.app.merchant.goods.bean.GoodsListEntity;
import com.olsplus.balancemall.app.merchant.goods.bean.ImageUploadEntity;
import com.olsplus.balancemall.app.merchant.goods.request.GoodsService;
import com.olsplus.balancemall.core.bean.BaseResultEntity;
import com.olsplus.balancemall.core.http.DoTransform;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.UrlConst;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class GoodsBusiness {

    private static GoodsService service = HttpManager.getRetrofit().create(GoodsService.class);

    /**
     * 获取商品列表
     *
     * @param context
     * @param observer
     */
    public static void getGoodsList(Context context, int page, int count, String type, HttpResultObserver<GoodsListEntity> observer) {

        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("type", type);
        paramMap.put("count", String.valueOf(count));
        paramMap.put("page", String.valueOf(page));
        String sign = HttpUtil.sign(HttpUtil.GET, UrlConst.merchant.goods_list, paramMap);


        service.getGoodsList(uid, token, sign, timestamp, local_service_id, type, page, count)
                .compose(DoTransform.<GoodsListEntity>applyScheduler())
                .subscribe(observer);
    }


    /**
     * 上架商品
     *
     * @param context
     * @param product_id
     * @param observer
     */
    public static void onSale(Context context, long product_id, HttpResultObserver<BaseResultEntity> observer) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("product_id", String.valueOf(product_id));
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.goods_on_sale, paramMap);

        service.onSale(uid, token, sign, timestamp, local_service_id, product_id)
                .compose(DoTransform.<BaseResultEntity>applyScheduler())
                .subscribe(observer);
    }

    /**
     * 下架商品
     *
     * @param context
     * @param product_id
     * @param observer
     */
    public static void offSale(Context context, long product_id, HttpResultObserver<BaseResultEntity> observer) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("product_id", String.valueOf(product_id));
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.goods_off_sale, paramMap);

        service.offSale(uid, token, sign, timestamp, local_service_id, product_id)
                .compose(DoTransform.<BaseResultEntity>applyScheduler())
                .subscribe(observer);
    }

    /**
     * 删除商品
     *
     * @param context
     * @param product_id
     * @param observer
     */
    public static void delete(Context context, long product_id, HttpResultObserver<BaseResultEntity> observer) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("product_id", String.valueOf(product_id));
        String sign = HttpUtil.sign(HttpUtil.POST, UrlConst.merchant.goods_delete, paramMap);

        service.delete(uid, token, sign, timestamp, local_service_id, product_id)
                .compose(DoTransform.<BaseResultEntity>applyScheduler())
                .subscribe(observer);
    }

    /**
     * 得到上传图片所需的token和文件名
     *
     * @param context
     * @param img
     */
    public static Observable<ImageUploadEntity> getGoodsToken(final Context context, final String img) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("img", img);
        String sign = HttpUtil.sign(HttpUtil.POST, ApiConst.BASE_URL + context.getString(R.string.url_upload_token, local_service_id), paramMap);

        return service.getGoodsToken(local_service_id, uid, token, sign, timestamp, img)
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

    /**
     * 添加商品
     *
     * @param context
     * @param goodsDetail
     * @param observer
     */
    public static void addGoods(Context context, GoodsDetail goodsDetail, HttpResultObserver<BaseResultEntity> observer) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.signWithJson(HttpUtil.POST, ApiConst.BASE_URL + context.getString(R.string.url_add_goods, local_service_id), paramMap, new Gson().toJson(goodsDetail));

        service.addGoods(local_service_id, uid, token, sign, timestamp, goodsDetail)
                .compose(DoTransform.<AddResultEntity>applyScheduler())
                .subscribe(observer);
    }

    /**
     * 编辑商品
     *
     * @param context
     * @param product_id
     * @param observer
     */
    public static void editGoods(Context context, long product_id, HttpResultObserver<EditGoodsEntity> observer) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String local_service_id = (String) SPUtil.get(context, SPUtil.LOCAL_SERVICE_ID, "");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        paramMap.put("local_service_id", local_service_id);
        paramMap.put("product_id", String.valueOf(product_id));
        String sign = HttpUtil.sign(HttpUtil.GET, UrlConst.merchant.goods_edit, paramMap);

        service.editGoods(uid, token, sign, timestamp, local_service_id, product_id)
                .compose(DoTransform.<EditGoodsEntity>applyScheduler())
                .subscribe(observer);
    }

    /**
     * 更新商品
     *
     * @param context
     * @param goodsDetail
     * @param observer
     */
    public static void updateGoods(Context context, GoodsDetail goodsDetail, HttpResultObserver<AddResultEntity> observer) {
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.signWithJson(HttpUtil.POST, UrlConst.merchant.goods_update, paramMap, new Gson().toJson(goodsDetail));

        service.updateGoods(uid, token, sign, timestamp, goodsDetail)
                .compose(DoTransform.<AddResultEntity>applyScheduler())
                .subscribe(observer);
    }
}
