package com.olsplus.balancemall.app.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.bottom.BottomNavigateFragment;
import com.olsplus.balancemall.app.login.LoginActivity;
import com.olsplus.balancemall.app.province.BuildCityActivity;
import com.olsplus.balancemall.app.upgrade.bean.UpdateInfo;
import com.olsplus.balancemall.app.upgrade.bean.UpdateResult;
import com.olsplus.balancemall.app.upgrade.dialog.DialogManager;
import com.olsplus.balancemall.app.upgrade.dialog.UpgradeDownloadDialog;
import com.olsplus.balancemall.app.upgrade.service.UpgradeService;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.event.TokenEvent;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ActivityManager;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeActivity extends MainActivity {

    private TextView titleTv;
    private CmsHomeFragment cmsHomeFragment;
    private UpgradeDownloadDialog upgradeDownloadDialog;
    private boolean isCompleted;

    @Override
    protected BaseFragment getFirstFragment() {
        cmsHomeFragment = CmsHomeFragment.getInstance();
        return cmsHomeFragment;
    }

    @Override
    protected int getTitleViewId() {
        return R.layout.home_title_bar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_main_container;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setBottomFragment(new BottomNavigateFragment());
        super.onCreate(savedInstanceState);
        titleTv = (TextView) findViewById(R.id.building_tv);
        String buildingName = (String) SPUtil.get(this, SPUtil.BUILDING_NAME, "");
        titleTv.setText(buildingName);
        titleTv.setOnClickListener(this);
        // 检查更新
        checkUpgrade();
    }

    /**
     * 是否下载完成
     *
     * @param isCompleted
     */
    @Subscribe
    public void onDownloadCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    protected void onRestart() {
        // 按Home键之后下载完成，返回界面让Dialog消失
        if (upgradeDownloadDialog != null && isCompleted) {
            upgradeDownloadDialog.dismiss();
            upgradeDownloadDialog = null;
            isCompleted = false;
        }
        super.onRestart();
    }

    private void checkUpgrade() {

        upgradeDownloadDialog = new UpgradeDownloadDialog();
        upgradeDownloadDialog.setCancelable(false);

        long time = (long) SPUtil.get(this, SPUtil.UPDATE_TIME, 0L);
        long currentTime = System.currentTimeMillis();

        // 正式 一天检查更新一次
        if (currentTime - time >= 24 * 3600 * 1000) {

            String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
            String url = ApiConst.BASE_URL + "v1/version";
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("channel", HttpUtil.CHANNEL);
            paramMap.put("platform", HttpUtil.PLATFORM);
            paramMap.put("timestamp", timestamp);
            String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);

            HttpManager.getRetrofit()
                    .create(UpgradeService.class)
                    .checkUpgrade(HttpUtil.PLATFORM, HttpUtil.CHANNEL, timestamp, sign)// 检查更新接口
                    .doOnNext(new Action1<UpdateResult>() {
                        @Override
                        public void call(UpdateResult updateResult) {
                            showDialog(HomeActivity.this, updateResult);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        }
    }

    private void showDialog(Context context, UpdateResult updateResult) {

        if (updateResult == null) {
            return;
        }

        UpdateInfo version = updateResult.getVersion();

//        if (BuildConfig.DEBUG) {
        // 测试
//            version = new UpdateInfo();
//            version.setUrl("http://dldir1.qq.com/weixin/android/weixin6331android940.apk");
//            version.setInfo("发现新版本，是否立即更新？");
//            version.setNumber("1.0.2");
//        }

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
                        // 小版本变化选择更新
                        DialogManager.upgradeNormal(context, version, upgradeDownloadDialog);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.building_tv:
                Intent intent = new Intent(this, BuildCityActivity.class);
                intent.putExtra("city_id", "1");
                intent.putExtra("city_name", "厦门");
                intent.putExtra("from", "");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String from = intent.getStringExtra("from");
            if ("0".equals(from)) {
                String buildingName = (String) SPUtil.get(this, SPUtil.BUILDING_NAME, "");
                titleTv.setText(buildingName);
                if (cmsHomeFragment != null) {
                    cmsHomeFragment.refreshHome();
                }
            }
        }
    }

    @Subscribe
    public void onUpdateTokenFailed(TokenEvent tokenEvent) {
        ActivityManager.getInstance().finishAllActivity();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        SPUtil.remove(this, SPUtil.UID);
        SPUtil.remove(this, SPUtil.TOKEN);
        SPUtil.remove(this, SPUtil.IS_FRIST);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
