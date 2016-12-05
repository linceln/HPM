package com.olsplus.balancemall.core.app;

import android.content.Intent;
import android.os.Bundle;

import com.olsplus.balancemall.app.login.LoginActivity;
import com.olsplus.balancemall.core.netstate.NetWorkUtil;
import com.olsplus.balancemall.core.util.ActivityManager;
import com.olsplus.balancemall.core.util.DialogHelper;
import com.olsplus.balancemall.core.util.ToastUtil;

public abstract class MainActivity extends BaseActivity {

    protected DialogHelper mDialogHelper;

    //获取第一个fragment
    protected abstract BaseFragment getFirstFragment();

    //获取Intent
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        MyApplication.getIntstance().onActivityCreated(this);
        mDialogHelper = new DialogHelper(this);
        setTitleResId(getTitleViewId());
        setContentView(getContentViewId());
        if (null != getIntent()) {
            handleIntent(getIntent());
        }

        //避免重复添加Fragment
        if (null == getSupportFragmentManager().getFragments()) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }

    }

    @Override
    protected int getTitleViewId() {
        return -1;
    }


    @Override
    protected int getContentViewId() {
        return -1;
    }

//    @Override
//    protected int getFragmentContentId() {
//        return 0;
//    }


    @Override
    protected void onResume() {
        super.onResume();
//        String buildingId = (String) SPUtil.get(this, SPUtil.BUILDING_ID, "");
//        if(TextUtils.isEmpty(buildingId)){
//            goLogin();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }

    public void goLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 网络连接连接时调用
     */
    public void onConnect(NetWorkUtil.netType type)
    {
        //to do

    }

    /**
     * 当前没有网络连接
     */
    public void onDisConnect()
    {
        ToastUtil.showShort(this,"网络连接已断开");
    }




}
