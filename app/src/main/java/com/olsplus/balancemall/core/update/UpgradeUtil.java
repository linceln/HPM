package com.olsplus.balancemall.core.update;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.component.dialog.DownloadDialog;
import com.olsplus.balancemall.component.dialog.UpgradeForceDialog;
import com.olsplus.balancemall.component.dialog.UpgradeNormalDialog;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import rx.Subscriber;


public class UpgradeUtil {

    public static void checkUpdate(final Context context) {
        // 保存检查更新时间
        SPUtil.put(context, SPUtil.UPDATE_TIME, System.currentTimeMillis());

        CheckUpdateBusiness.checkUpdate(new UpdateCallback() {
            @Override
            public void onSuccess(UpdateResult data) {

                UpdateInfo version = data.getVersion();

                if (BuildConfig.DEBUG) {
                    upgradeNormal(context, "http://file1.updrv.com/soft/2016/20161208/drivethelife6_setup.exe");
                }

                if (version != null) {
                    // 当前版本
                    LogUtil.e("Update", "Local Version: v" + AppUtil.getVersionName(context));
                    // 云端版本
                    LogUtil.e("Update", "Cloud Version: v" + data.getVersion().getNumber());

                    String versionNum = version.getNumber();
                    if (!TextUtils.isEmpty(versionNum)) {
                        String cloudValues[] = versionNum.split("\\.");
                        if (null != cloudValues && cloudValues.length >= 2) {
                            String currentVersion = AppUtil.getVersionName(context);
                            String currentValues[] = currentVersion.split("\\.");
                            int one = Integer.parseInt(currentValues[0]);
                            int two = Integer.parseInt(currentValues[1]);
                            int three = Integer.parseInt(currentValues[2]);

                            int one1 = Integer.parseInt(cloudValues[0]);
                            int two2 = Integer.parseInt(cloudValues[1]);
                            int three3 = Integer.parseInt(cloudValues[2]);
                            if (one1 > one) {
                                // 大版本变化强制更新
                                upgradeForce(context, version.getUrl());
                            } else if (two2 > two) {
                                // 小版本变化选择更新
                                upgradeNormal(context, version.getUrl());
                            } else if (two2 == two && three3 > three) {
                                upgradeNormal(context, version.getUrl());
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 选择更新
     *
     * @param context
     */
    public static void upgradeNormal(final Context context, final String url) {
        final UpgradeNormalDialog upgradeNormalDialog = new UpgradeNormalDialog();
        upgradeNormalDialog.setMessage(context.getString(R.string.upgrade_normal_msg));
        upgradeNormalDialog.setOnPositiveClickListener(new UpgradeNormalDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                // 跳转下载进度Dialog
                upgradeNormalDialog.dismiss();
                downloadApk(context, url);
            }
        });
        if (context instanceof AppCompatActivity) {
            upgradeNormalDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "UpgradeNormal");
        }
    }

    /**
     * 强制更新
     *
     * @param context
     */
    public static void upgradeForce(final Context context, final String url) {
        final UpgradeForceDialog generalDialogFragment = new UpgradeForceDialog();
        generalDialogFragment.setMessage(context.getString(R.string.upgrade_force_msg));
        generalDialogFragment.setCancelable(false);
        generalDialogFragment.setOnPositiveClickListener(new UpgradeForceDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                // 跳转下载进度Dialog
                downloadApk(context, url);
            }
        });
        if (context instanceof AppCompatActivity) {
            generalDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "UpgradeForce");
        }
    }

    /**
     * 下载APK
     *
     * @param url
     */
    private static void downloadApk(final Context context, String url) {

//        // 下载进度Dialog
        final DownloadDialog downloadDialog = new DownloadDialog();
        downloadDialog.setCancelable(false);
        downloadDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "");

        CheckUpdateBusiness.downloadApk(context, url, downloadDialog);
    }

}
