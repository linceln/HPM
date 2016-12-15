package com.olsplus.balancemall.core.update;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.component.dialog.DownloadDialog;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.FileUtil;
import com.olsplus.balancemall.core.util.LogUtil;

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
import rx.functions.Func1;
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
                .map(new Func1<ResponseBody, Boolean>() {
                    @Override
                    public Boolean call(ResponseBody responseBody) {
                        // IO线程写入文件
                        return writeResponseBodyToDisk(context, responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        if (!AppUtil.isBackground(context)) {
                            if (downloadDialog != null) {
                                downloadDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!AppUtil.isBackground(context)) {
                            if (downloadDialog != null) {
                                downloadDialog.dismiss();
                            }
                        }
                        LogUtil.e("DownloadAPK", e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean downloadCompleted) {
                    }
                });
    }

    private static boolean writeResponseBodyToDisk(Context context, ResponseBody body) {

        // APK文件缓存路径
        File apkFile = FileUtil.cacheApkFile(context);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[1024];
            // 文件大小
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = body.byteStream();
            outputStream = new FileOutputStream(apkFile);

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // SDK24以上
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                // SDK24以下
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            // TODO 安装完成删除安装包，需要在Activity中回调
//            if (requestCode == REQUEST_INSTALL) {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (apkFile.exists()) {
//                        apkFile.delete();
//                    }
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//            } else {
//            }


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
