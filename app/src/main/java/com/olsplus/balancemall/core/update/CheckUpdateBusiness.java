package com.olsplus.balancemall.core.update;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.olsplus.balancemall.component.dialog.DownloadDialog;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CheckUpdateBusiness {

    public static void checkUpdate(final UpdateCallback updateCallback) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/version";
        String sign = parseUpdateSign(url, timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<UpdateResult>() {

            @Override
            public void prepare() {
            }

            @Override
            public void reConnect() {

            }

            @Override
            public void handleSuccessResp(UpdateResult data) {
                if (updateCallback != null) {
                    updateCallback.onSuccess(data);
                }
            }

            @Override
            public void handleFailedResp(String msg) {
                if (updateCallback != null) {
                    updateCallback.onError(msg);
                }
            }
        };
        HttpManager.getRetrofit()
                .create(CheckUpgradeService.class)
                .checkUpgrade(HttpUtil.PLATFORM, HttpUtil.CHANNEL, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    private static String parseUpdateSign(String url, String timestamp) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("channel", HttpUtil.CHANNEL);
        paramMap.put("platform", HttpUtil.PLATFORM);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    /**
     * 下载APK
     */
    public static void downloadApk(final Context context, String url, final DownloadDialog downloadDialog) {
        HttpManager.getRetrofit()
                .create(CheckUpgradeService.class)
                .downloadApk(url)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        // IO线程写入文件
                        boolean b = writeResponseBodyToDisk(context, responseBody);
                        EventBus.getDefault().post(b);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        if (!AppUtil.isBackground(context)) {
                            if (downloadDialog != null) {
                                downloadDialog.dismiss();
                            }
                        }
//                        LogUtil.e("onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!AppUtil.isBackground(context)) {
                            if (downloadDialog != null) {
                                downloadDialog.dismiss();
                            }
                        }
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        LogUtil.e("onError", "onError" + e.getMessage());

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
//                        LogUtil.e("onNext", "onNext" + responseBody.contentLength());
                    }
                });
    }

    private static boolean writeResponseBodyToDisk(Context context, ResponseBody body) {

        // APK文件缓存路径
        File futureStudioIconFile = new File(context.getExternalCacheDir(), "upgrade.apk");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[1024];
            // 文件大小
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(futureStudioIconFile);

            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                // 已下载文件大小
                fileSizeDownloaded += read;
            }
            outputStream.flush();
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(futureStudioIconFile), "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
            return true;
        } catch (IOException e) {
            return false;
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void downloadApk(Context context, UpdateInfo updateInfo, String infoName, String storeApk) {
//        if (!isDownloadManagerAvailable()) {
//            return;
//        }
//
//        String description = updateInfo.getInfo();
//        String appUrl = updateInfo.getUrl();
//
//        if (appUrl == null || appUrl.isEmpty()) {
//            Log.e("yongyuan.w", "请填写\"App下载地址\"");
//            return;
//        }
//
//        appUrl = appUrl.trim(); // 去掉首尾空格
//
//        if (!appUrl.startsWith("http")) {
//            appUrl = "http://" + appUrl; // 添加Http信息
//        }
//
//        Log.e("yongyuan.w", "appUrl: " + appUrl);
//
//        DownloadManager.Request request;
//        try {
//            request = new DownloadManager.Request(Uri.parse(appUrl));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//        request.setTitle(infoName);
//        request.setDescription(description);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        }
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, storeApk);
//
//        Context appContext = context.getApplicationContext();
//        DownloadManager manager = (DownloadManager)
//                appContext.getSystemService(Context.DOWNLOAD_SERVICE);
//
//        // 存储下载Key
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
//        sp.edit().putLong(SPUtil.DOWNLOAD_APK_ID_PREFS, manager.enqueue(request)).apply();
//    }
//
//    // 最小版本号大于9
//    private static boolean isDownloadManagerAvailable() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
//    }
}
