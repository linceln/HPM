package com.olsplus.balancemall.app.mine.config;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.login.LoginActivity;
import com.olsplus.balancemall.app.login.request.LoginRequestImpl;
import com.olsplus.balancemall.app.login.view.IloginOutView;
import com.olsplus.balancemall.app.mine.UserInfoActivity;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.app.MainActivity;
import com.olsplus.balancemall.core.util.ActivityManager;
import com.olsplus.balancemall.core.util.ImageCatchUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

public class SettingActivity extends MainActivity implements IloginOutView {

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private LinearLayout userInfoLayout;
    private LinearLayout passwordLayout;
    private LinearLayout messageLayout;
    private CheckBox msgNeedCb;
    private LinearLayout cacheLayout;
    private Button exitBtn;

    private LoginRequestImpl loginRequestImpl;

    @Override
    protected BaseFragment getFirstFragment() {
        return null;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected int getTitleViewId() {
        return R.layout.action_bar_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.mystore_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
        mTitleName.setText("设置");
        userInfoLayout = (LinearLayout) findViewById(R.id.mysettting_account_layout);
        passwordLayout = (LinearLayout) findViewById(R.id.mysettting_password_layout);
        messageLayout = (LinearLayout) findViewById(R.id.mysettting_msg_layout);
        msgNeedCb = (CheckBox) findViewById(R.id.mysettting_msg_check_need);
        cacheLayout = (LinearLayout) findViewById(R.id.mysettting_clean_cache_layout);
        exitBtn = (Button) findViewById(R.id.setting_logout_btn);
        userInfoLayout.setOnClickListener(this);
        passwordLayout.setOnClickListener(this);
        messageLayout.setOnClickListener(this);
        msgNeedCb.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        cacheLayout.setOnClickListener(this);

        loginRequestImpl = new LoginRequestImpl(this);
        loginRequestImpl.setIloginOutView(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.mysettting_account_layout:
                intent = new Intent(this, UserInfoActivity.class);
                startActivity(intent);
                break;

            case R.id.mysettting_password_layout:
                intent = new Intent(this, PasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.mysettting_msg_layout:
                break;

            case R.id.mysettting_msg_check_need:
                break;

            case R.id.mysettting_clean_cache_layout:
                try {
                    clearImageCache();
                    clearWebCache();
                } catch (Throwable e) {
                    e.printStackTrace();
                    ToastUtil.showShort(this, "清除失败");
                    return;
                }
                ToastUtil.showShort(this, "清除成功");
                break;
            case R.id.setting_logout_btn:
                loginRequestImpl.loginOut();

                break;
        }
    }

    private void clearImageCache() {
        ImageCatchUtil.getInstance().clearImageAllCache();
    }

    private void clearWebCache() {
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");

        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e("yongyuan.w", "delete file no exists " + file.getAbsolutePath());
        }
    }

    @Override
    public void showLogoutError(String msg) {
        ToastUtil.showShort(this, "退出登录失败");
    }

    @Override
    public void onLogoutSuccess() {
        ActivityManager.getInstance().finishAllActivity();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
//        SPUtil.clear(this);
        SPUtil.remove(this, SPUtil.UID);
        SPUtil.remove(this, SPUtil.TOKEN);
        SPUtil.remove(this, SPUtil.IS_FRIST);

        // 友盟账号统计
        MobclickAgent.onProfileSignOff();
    }
}