package com.olsplus.balancemall.app.upgrade.dialog;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.olsplus.balancemall.app.upgrade.bean.UpdateInfo;
import com.olsplus.balancemall.component.dialog.GeneralDialogFragment;
import com.olsplus.balancemall.core.util.NetworkUtil;

import static com.olsplus.balancemall.app.upgrade.DownloadManager.downloadApk;


public class DialogManager {

    private DialogManager() {
    }

    /**
     * 选择更新
     *
     * @param context
     * @param version
     * @param upgradeDownloadDialog
     */
    public static void upgradeNormal(final Context context, final UpdateInfo version, final UpgradeDownloadDialog upgradeDownloadDialog) {
        final UpgradeNormalDialog upgradeNormalDialog = new UpgradeNormalDialog();
        upgradeNormalDialog.setMessage(version.getInfo());
        upgradeNormalDialog.setOnPositiveClickListener(new UpgradeNormalDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                // 判断网络
                upgradeNormalDialog.dismiss();
                checkNetwork(context, version.getUrl(), upgradeDownloadDialog);
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
     * @param upgradeDownloadDialog
     */
    public static void upgradeForce(final Context context, final UpdateInfo version, final UpgradeDownloadDialog upgradeDownloadDialog) {
        final UpgradeForceDialog generalDialogFragment = new UpgradeForceDialog();
        generalDialogFragment.setMessage(version.getInfo());
        generalDialogFragment.setCancelable(false);
        generalDialogFragment.setOnPositiveClickListener(new UpgradeForceDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                // 判断网络
                checkNetwork(context, version.getUrl(), upgradeDownloadDialog);
            }
        });
        if (context instanceof AppCompatActivity) {
            generalDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "UpgradeForce");
        }
    }

    /**
     * 判断网络是否wifi连接
     *
     * @param url
     * @param upgradeDownloadDialog
     */
    private static void checkNetwork(final Context context, final String url, final UpgradeDownloadDialog upgradeDownloadDialog) {

        //  判断网络连接
        if (NetworkUtil.isWifiConnected(context)) {// wifi连接

            // 下载进度Dialog
            upgradeDownloadDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "下载进度");

            downloadApk(context, url, upgradeDownloadDialog);
        } else {
            GeneralDialogFragment generalDialogFragment = new GeneralDialogFragment();
            generalDialogFragment.setMessage("当前使用手机流量联网，是否继续更新？");
            generalDialogFragment.setOnPositiveClickListener(new GeneralDialogFragment.OnPositiveClickListener() {
                @Override
                public void onClick() {
                    // 下载进度Dialog
                    upgradeDownloadDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "下载进度");

                    downloadApk(context, url, upgradeDownloadDialog);
                }
            });
            generalDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "");
        }
    }
}
