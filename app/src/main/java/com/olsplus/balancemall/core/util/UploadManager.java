package com.olsplus.balancemall.core.util;


import android.content.Context;

import com.olsplus.balancemall.app.merchant.goods.bean.ImageUploadEntity;
import com.olsplus.balancemall.app.merchant.goods.business.GoodsBusiness;
import com.olsplus.balancemall.app.mine.bussiness.MyOrderBussiness;
import com.olsplus.balancemall.app.mine.bussiness.UserBusiness;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.olsplus.balancemall.core.util.BitmapUtil.compressSample;

public final class UploadManager {

    private static final String TAG = "UploadManager ==> ";

    private UploadManager() {
        throw new UnsupportedOperationException("can not be instantiated");
    }

    /**
     * 上传头像
     *
     * @param context      RxAppCompatActivity
     * @param originalPath 原始图片
     * @param subscriber
     */
    public static void uploadAvatar(final Context context, String originalPath, final Subscriber<String> subscriber) {

        Observable.just(originalPath) // 发送原图地址
                .subscribeOn(Schedulers.io()) // 压缩过程在子线程执行
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String originalPath) {

                        LogUtil.e(TAG + "Original Path", originalPath);
                        LogUtil.e(TAG + "Original Length", new File(originalPath).length() / 1024 + "K");
                        // 映射为压缩后的路径，耗时，在子线程执行，压缩后的图片保存在缓存文件夹下
                        return compressSample(context, originalPath);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {

                        // 压缩异常处理
                        if (s == null) {
                            return Observable.error(new Exception("图片不存在"));
                        }
                        return Observable.just(s);
                    }
                })
                .flatMap(new Func1<String, Observable<ImageUploadEntity>>() {
                    @Override
                    public Observable<ImageUploadEntity> call(String compressedPath) {

                        LogUtil.e(TAG + "Compressed Path", compressedPath);
                        LogUtil.e(TAG + "Compressed Length", new File(compressedPath).length() / 1024 + "K");
                        // 用压缩后的路径请求token和文件名
                        return new UserBusiness(context).getAvatarToken(compressedPath);
                    }
                })
                .flatMap(new Func1<ImageUploadEntity, Observable<String>>() {
                    @Override
                    public Observable<String> call(final ImageUploadEntity imageUploadEntity) {
                        // 通过得到的token和文件名将图片上传到七牛服务器
                        return upload2QiNiu(imageUploadEntity);
                    }
                })
                // RxLifecycle
                .compose(((RxAppCompatActivity) context).<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 上传商品图片
     *
     * @param context
     * @param originalPaths 图片原始路径集合
     * @param subscriber
     */
    public static void uploadGoodsImage(final Context context, final List<String> originalPaths, final Subscriber<String> subscriber) {

        Observable.from(originalPaths) // 发送原图地址
                .subscribeOn(Schedulers.io()) // 压缩过程在子线程执行
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String originalPath) {

                        LogUtil.e(TAG + "Original Path", originalPath);
                        LogUtil.e(TAG + "Original Length", new File(originalPath).length() / 1024 + "K");
                        // 映射为压缩后的路径，耗时，在子线程执行，压缩后的图片保存在缓存文件夹下
                        return compressSample(context, originalPath);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {

                        // 压缩异常处理
                        if (s == null) {
                            return Observable.error(new Exception("图片不存在"));
                        }
                        return Observable.just(s);
                    }
                })
                .flatMap(new Func1<String, Observable<ImageUploadEntity>>() {
                    @Override
                    public Observable<ImageUploadEntity> call(String compressedPath) {

                        LogUtil.e(TAG + "Compressed Path", compressedPath);
                        LogUtil.e(TAG + "Compressed Length", new File(compressedPath).length() / 1024 + "K");
                        // 用压缩后的路径请求token和文件名
                        return GoodsBusiness.getGoodsToken(context, compressedPath);
                    }
                })
                .flatMap(new Func1<ImageUploadEntity, Observable<String>>() {
                    @Override
                    public Observable<String> call(final ImageUploadEntity imageUploadEntity) {
                        // 通过得到的token和文件名将图片上传到七牛服务器
                        return upload2QiNiu(imageUploadEntity);
                    }
                })
                //  RxLifecycle
                .compose(((RxAppCompatActivity) context).<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 上传退款凭据
     *
     * @param context
     * @param originalPaths 原始图片集合
     * @param subscriber
     */
    public static void uploadProof(final Context context, final List<String> originalPaths, final Subscriber<String> subscriber) {

        Observable.from(originalPaths) // 发送原图地址
                .subscribeOn(Schedulers.io()) // 压缩过程在子线程执行
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String originalPath) {

                        LogUtil.e(TAG + "Original Path", originalPath);
                        LogUtil.e(TAG + "Original Length", new File(originalPath).length() / 1024 + "K");
                        // 映射为压缩后的路径，耗时，在子线程执行，压缩后的图片保存在缓存文件夹下
                        return compressSample(context, originalPath);
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {

                        // 压缩异常处理
                        if (s == null) {
                            return Observable.error(new Exception("图片不存在"));
                        }
                        return Observable.just(s);
                    }
                })
                .flatMap(new Func1<String, Observable<ImageUploadEntity>>() {
                    @Override
                    public Observable<ImageUploadEntity> call(String compressedPath) {

                        LogUtil.e(TAG + "Compressed Path", compressedPath);
                        LogUtil.e(TAG + "Compressed Length", new File(compressedPath).length() / 1024 + "K");
                        // 用压缩后的路径请求token和文件名
                        return new MyOrderBussiness(context).getProofToken(compressedPath);
                    }
                })
                .flatMap(new Func1<ImageUploadEntity, Observable<String>>() {
                    @Override
                    public Observable<String> call(final ImageUploadEntity imageUploadEntity) {
                        // 通过得到的token和文件名将图片上传到七牛服务器
                        return upload2QiNiu(imageUploadEntity);
                    }
                })
                //  RxLifecycle
                .compose(((RxAppCompatActivity) context).<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    /**
     * 上传到七牛服务器
     *
     * @param entity
     * @return
     */
    private static Observable<String> upload2QiNiu(final ImageUploadEntity entity) {

        return Observable.fromEmitter(new Action1<Emitter<String>>() { // 封装异步操作为Observable
            @Override
            public void call(final Emitter<String> stringEmitter) {
                QiNiuUtil.getInstance().put(entity.getCompressedPath(), entity.getFilepath(), entity.getToken(), new UpCompletionHandler() {
                    @Override
                    public void complete(String s, final ResponseInfo responseInfo, JSONObject jsonObject) {
                        if (responseInfo.isOK()) {
                            LogUtil.e(TAG, "上传成功 耗时:" + responseInfo.duration);
                            // 上传成功，返回图片url
                            stringEmitter.onNext(s);
                            // 上传完成
                            stringEmitter.onCompleted();
                        } else {
                            LogUtil.e(TAG, "上传失败 " + responseInfo.error);
                            stringEmitter.onError(new Throwable(responseInfo.error));
                        }
                    }
                }, null);
            }
        }, Emitter.BackpressureMode.BUFFER);
    }
}