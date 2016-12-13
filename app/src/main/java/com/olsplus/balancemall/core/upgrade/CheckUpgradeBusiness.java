package com.olsplus.balancemall.core.upgrade;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.update.UpdateResult;
import com.olsplus.balancemall.core.update.CheckUpgradeService;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CheckUpgradeBusiness {

    private static CheckUpgradeService service = HttpManager.getRetrofit().create(CheckUpgradeService.class);

    private static void checkUpgrade(final Context context) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/version";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("channel", HttpUtil.CHANNEL);
        paramMap.put("platform", HttpUtil.PLATFORM);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);

        service.checkUpgrade(HttpUtil.PLATFORM, HttpUtil.CHANNEL, timestamp, sign)// 检查更新接口
                .doOnNext(new Action1<UpdateResult>() {
                    @Override
                    public void call(UpdateResult updateResult) {

                    }
                })
                .flatMap(new Func1<UpdateResult, rx.Observable<ResponseBody>>() {
                    @Override
                    public rx.Observable<ResponseBody> call(UpdateResult updateResult) {
                        if (updateResult.getVersion() != null && !TextUtils.isEmpty(updateResult.getVersion().getUrl())) {
                            // 下载文件


                            return service.downloadApk(updateResult.getVersion().getUrl());
                        } else {
                            // 不需要更新
                            return rx.Observable.error(new Exception(""));
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        // 写入文件
                        writeResponseBodyToDisk(context, responseBody);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
}
