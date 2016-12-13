package com.olsplus.balancemall.core.update;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.component.dialog.DownloadDialog;
import com.olsplus.balancemall.component.dialog.GeneralDialogFragment;
import com.olsplus.balancemall.component.dialog.UpgradeForceDialog;
import com.olsplus.balancemall.component.dialog.UpgradeNormalDialog;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.NetworkUtil;
import com.olsplus.balancemall.core.util.SPUtil;


public class UpgradeUtil {

    public static void checkUpdate(final Context context, final DownloadDialog downloadDialog) {
        // 保存检查更新时间
        SPUtil.put(context, SPUtil.UPDATE_TIME, System.currentTimeMillis());

        CheckUpdateBusiness.checkUpdate(new UpdateCallback() {
            @Override
            public void onSuccess(UpdateResult data) {

                UpdateInfo version = data.getVersion();

                if (BuildConfig.DEBUG) {
                    // 测试
                    version = new UpdateInfo();
                    version.setUrl("http://dldir1.qq.com/weixin/android/weixin6331android940.apk");
                    version.setInfo("最新版本更新！！！！！！！");
                    version.setNumber("2.0.2");
                }

                if (version != null) {
                    // 当前版本
                    LogUtil.e("Update", "Local Version: v" + AppUtil.getVersionName(context));
                    // 云端版本
                    LogUtil.e("Update", "Cloud Version: v" + version.getNumber());

                    String versionNum = version.getNumber();
                    if (!TextUtils.isEmpty(versionNum)) {
                        String cloudValues[] = versionNum.split("\\.");
                        if (cloudValues.length >= 2) {
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
                                upgradeForce(context, version, downloadDialog);
                            } else if (two2 > two) {
                                // 小版本变化选择更新
                                upgradeNormal(context, version, downloadDialog);
                            } else if (two2 == two && three3 > three) {
                                upgradeNormal(context, version, downloadDialog);
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
     * @param version
     * @param downloadDialog
     */
    public static void upgradeNormal(final Context context, final UpdateInfo version, final DownloadDialog downloadDialog) {
        final UpgradeNormalDialog upgradeNormalDialog = new UpgradeNormalDialog();
//        upgradeNormalDialog.setMessage(context.getString(R.string.upgrade_normal_msg));
        upgradeNormalDialog.setMessage(version.getInfo());
        upgradeNormalDialog.setOnPositiveClickListener(new UpgradeNormalDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                // 跳转下载进度Dialog
                upgradeNormalDialog.dismiss();
                downloadApk(context, version.getUrl(), downloadDialog);
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
     * @param version
     * @param downloadDialog
     */
    public static void upgradeForce(final Context context, final UpdateInfo version, final DownloadDialog downloadDialog) {
        final UpgradeForceDialog generalDialogFragment = new UpgradeForceDialog();
//        generalDialogFragment.setMessage(context.getString(R.string.upgrade_force_msg));
        generalDialogFragment.setMessage(version.getInfo());
        generalDialogFragment.setCancelable(false);
        generalDialogFragment.setOnPositiveClickListener(new UpgradeForceDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                // 跳转下载进度Dialog
                downloadApk(context, version.getUrl(), downloadDialog);
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
     * @param downloadDialog
     */
    private static void downloadApk(final Context context, final String url, final DownloadDialog downloadDialog) {

        //  判断网络连接
        if (NetworkUtil.isWifiConnected(context)) {// wifi连接

            // 下载进度Dialog
            downloadDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "下载进度");

            CheckUpdateBusiness.downloadApk(context, url, downloadDialog);
        } else {
            GeneralDialogFragment generalDialogFragment = new GeneralDialogFragment();
            generalDialogFragment.setMessage("当前使用手机流量联网，是否继续更新？");
            generalDialogFragment.setOnPositiveClickListener(new GeneralDialogFragment.OnPositiveClickListener() {
                @Override
                public void onClick() {
                    // 下载进度Dialog
                    downloadDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "下载进度");

                    CheckUpdateBusiness.downloadApk(context, url, downloadDialog);
                }
            });
            generalDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "");
        }
    }
}
