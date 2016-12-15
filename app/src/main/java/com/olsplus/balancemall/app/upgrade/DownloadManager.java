package com.olsplus.balancemall.app.upgrade;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.app.upgrade.dialog.UpgradeDownloadDialog;
import com.olsplus.balancemall.app.upgrade.service.UpgradeService;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.FileUtil;
import com.olsplus.balancemall.core.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DownloadManager {

    /**
     * 下载APK
     */
    public static void downloadApk(final Context context, String url, final UpgradeDownloadDialog upgradeDownloadDialog) {
        HttpManager.getDownloadRetrofit()
                .create(UpgradeService.class)
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
                            // 进程在前台
                            if (upgradeDownloadDialog != null) {
                                upgradeDownloadDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!AppUtil.isBackground(context)) {
                            // 进程在前台
                            if (upgradeDownloadDialog != null) {
                                upgradeDownloadDialog.dismiss();
                            }
                        }
                        LogUtil.e("DownloadAPK", e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean downloadCompleted) {
                    }
                });
    }

    /**
     * 写入文件
     *
     * @param context
     * @param body
     * @return
     */
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

            // 下载完成给主界面发消息
            EventBus.getDefault().post(true);
            // 打开安装程序页面
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
}
