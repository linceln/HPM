package com.olsplus.balancemall.app.upgrade.business;


import android.content.Context;
import android.text.TextUtils;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.app.upgrade.bean.UpdateInfo;
import com.olsplus.balancemall.app.upgrade.bean.UpdateResult;
import com.olsplus.balancemall.app.upgrade.dialog.DialogManager;
import com.olsplus.balancemall.app.upgrade.dialog.UpgradeDownloadDialog;
import com.olsplus.balancemall.app.upgrade.service.UpgradeService;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UpgradeBusiness {

    private static UpgradeService service = HttpManager.getRetrofit().create(UpgradeService.class);

    public static void checkUpgrade(final Context context, final UpgradeDownloadDialog upgradeDownloadDialog) {
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String url = ApiConst.BASE_URL + "v1/version";

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("channel", HttpUtil.CHANNEL);
        paramMap.put("platform", HttpUtil.PLATFORM);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);

        service.checkUpgrade(HttpUtil.PLATFORM, HttpUtil.CHANNEL, timestamp, sign)// 检查更新接口
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<UpdateResult>() {
                    @Override
                    public void call(UpdateResult updateResult) {
                        showDialog(context, updateResult, upgradeDownloadDialog);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private static void showDialog(Context context, UpdateResult updateResult, UpgradeDownloadDialog upgradeDownloadDialog) {

        if (updateResult == null) {
            return;
        }

        UpdateInfo version = updateResult.getVersion();

        if (BuildConfig.DEBUG) {
            // 测试
            version = new UpdateInfo();
            version.setUrl("http://dldir1.qq.com/weixin/android/weixin6331android940.apk");
            version.setInfo("发现新版本，是否立即更新？");
            version.setNumber("1.0.2");
        }

        if (version != null) {
            // 当前版本
            LogUtil.e("检查更新", "Local Version: v" + AppUtil.getVersionName(context));
            // 云端版本
            LogUtil.e("检查更新", "Cloud Version: v" + version.getNumber());
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
                        DialogManager.upgradeForce(context, version, upgradeDownloadDialog);
                    } else if (two2 > two) {
                        // 小版本变化选择更新
                        DialogManager.upgradeNormal(context, version, upgradeDownloadDialog);
                    } else if (two2 == two && three3 > three) {
                        DialogManager.upgradeNormal(context, version, upgradeDownloadDialog);
                    }
                }
            }
        }
    }
}
