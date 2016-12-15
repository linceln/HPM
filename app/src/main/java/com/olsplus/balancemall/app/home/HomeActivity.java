package com.olsplus.balancemall.app.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.bottom.BottomNavigateFragment;
import com.olsplus.balancemall.app.login.LoginActivity;
import com.olsplus.balancemall.app.province.BuildCityActivity;
import com.olsplus.balancemall.app.upgrade.business.CheckUpgradeBusiness;
import com.olsplus.balancemall.app.upgrade.dialog.DownloadDialog;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.event.TokenEvent;
import com.olsplus.balancemall.core.util.ActivityManager;
import com.olsplus.balancemall.core.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class HomeActivity extends MainActivity {

    private TextView titleTv;
    private CmsHomeFragment cmsHomeFragment;
    private DownloadDialog downloadDialog;
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
    public void isDownloadCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    protected void onRestart() {
        // 按Home键之后下载完成，返回界面Dialog消失
        if (downloadDialog != null && isCompleted) {
            downloadDialog.dismiss();
            downloadDialog = null;
        }
        super.onRestart();
    }

    private void checkUpgrade() {

        downloadDialog = new DownloadDialog();
        downloadDialog.setCancelable(false);

        long time = (long) SPUtil.get(this, SPUtil.UPDATE_TIME, 0L);
        long currentTime = System.currentTimeMillis();

        if (BuildConfig.DEBUG) {
            // 测试
            CheckUpgradeBusiness.checkUpgrade(this, downloadDialog);
        } else {
            // 正式 一天检查更新一次
            if (currentTime - time >= 24 * 3600 * 1000) {
                CheckUpgradeBusiness.checkUpgrade(this, downloadDialog);
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
