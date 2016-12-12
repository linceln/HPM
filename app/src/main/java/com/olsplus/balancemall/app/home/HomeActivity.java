package com.olsplus.balancemall.app.home;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguangqiang.permissionhelper.PermissionHelper;
import com.olsplus.balancemall.BuildConfig;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.bottom.BottomNavigateFragment;
import com.olsplus.balancemall.app.login.LoginActivity;
import com.olsplus.balancemall.app.province.BuildCityActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.event.TokenEvent;
import com.olsplus.balancemall.core.update.UpgradeUtil;
import com.olsplus.balancemall.core.util.ActivityManager;
import com.olsplus.balancemall.core.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

public class HomeActivity extends MainActivity {

    private TextView titleTv;
    private CmsHomeFragment cmsHomeFragment;

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
        EventBus.getDefault().register(this);
        setBottomFragment(new BottomNavigateFragment());
        super.onCreate(savedInstanceState);
        titleTv = (TextView) findViewById(R.id.building_tv);
        String buildingName = (String) SPUtil.get(this, SPUtil.BUILDING_NAME, "");
        titleTv.setText(buildingName);
        titleTv.setOnClickListener(this);
        // 友盟统计需要的权限
//        requestPhoneState();
    }

    private void requestPhoneState() {
        PermissionHelper.getInstance().requestPermission(this, Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long time = (Long) SPUtil.get(this, SPUtil.UPDATE_TIME, 0L);
        long currentTime = System.currentTimeMillis();

        if (BuildConfig.DEBUG) {
            UpgradeUtil.checkUpdate(this);
        } else {
            if (currentTime - time >= 24 * 3600 * 1000) {
                // 一天检查更新一次
                UpgradeUtil.checkUpdate(this);
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
    public void onWxPayEvent(TokenEvent tokenEvent) {
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
        EventBus.getDefault().unregister(this);
    }

    private static class UpgradeHandler extends Handler {

        private final WeakReference<AppCompatActivity> weakReference;

        public UpgradeHandler(AppCompatActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference != null) {
                AppCompatActivity activity = weakReference.get();
                switch (msg.what) {
                    case 0:
                        Toast.makeText(activity, (String) msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:

                        break;
                }
            }
        }
    }
}
