package com.olsplus.balancemall.core.update;


import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;


import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
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
                    updateCallback.onError();
                }
            }
        };
        HttpManager.getRetrofit()
                .create(UpdateService.class)
                .updateApp(HttpUtil.PLATFORM, HttpUtil.CHANNEL, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    private static String parseUpdateSign(String url, String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("channel", HttpUtil.CHANNEL);
        paramMap.put("platform", HttpUtil.PLATFORM);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    public static void downloadApk(
            Context context, UpdateInfo updateInfo,
            String infoName, String storeApk) {
        if (!isDownloadManagerAvailable()) {
            return;
        }

        String description = updateInfo.getInfo();
        String appUrl = updateInfo.getUrl();

        if (appUrl == null || appUrl.isEmpty()) {
            Log.e("yongyuan.w", "请填写\"App下载地址\"");
            return;
        }

        appUrl = appUrl.trim(); // 去掉首尾空格

        if (!appUrl.startsWith("http")) {
            appUrl = "http://" + appUrl; // 添加Http信息
        }

        Log.e("yongyuan.w", "appUrl: " + appUrl);

        DownloadManager.Request request;
        try {
            request = new DownloadManager.Request(Uri.parse(appUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        request.setTitle(infoName);
        request.setDescription(description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, storeApk);

        Context appContext = context.getApplicationContext();
        DownloadManager manager = (DownloadManager)
                appContext.getSystemService(Context.DOWNLOAD_SERVICE);

        // 存储下载Key
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);
        sp.edit().putLong(SPUtil.DOWNLOAD_APK_ID_PREFS, manager.enqueue(request)).apply();
    }

    // 最小版本号大于9
    private static boolean isDownloadManagerAvailable() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }
}
