package com.olsplus.balancemall.core.app;

import android.app.Application;

import com.olsplus.balancemall.core.netstate.NetChangeObserver;
import com.olsplus.balancemall.core.netstate.NetWorkUtil;
import com.olsplus.balancemall.core.netstate.NetworkStateReceiver;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class MyApplication extends Application {

    private static MyApplication mApplication;
    private Boolean networkAvailable = false;
    private NetChangeObserver netChangeObserver;
    private MainActivity  currentActivity;

    public static MyApplication getApp() {
        if (mApplication != null && mApplication instanceof MyApplication) {
            return (MyApplication) mApplication;
        } else {
            mApplication = new MyApplication();
            mApplication.onCreate();
            return (MyApplication) mApplication;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        //配置是否显示log
        LogUtil.isDebug = true;

        //配置时候显示toast
        ToastUtil.isShow = true;

        //配置程序异常退出处理
//        Thread.setDefaultUncaughtExceptionHandler(new LocalFileHandler(this));

        initNetwork();
    }

    public  OkHttpClient defaultOkHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
        return client;
    }

    public static MyApplication getIntstance() {
        return mApplication;
    }

    public void onActivityCreated(MainActivity mainActivity){
        this.currentActivity = mainActivity;
    }

    private void initNetwork(){
        networkAvailable = NetWorkUtil.isNetworkConnected(getApplicationContext());
        netChangeObserver = new NetChangeObserver() {
            @Override
            public void onConnect(NetWorkUtil.netType type) {
                super.onConnect(type);
                MyApplication.this.onConnect(type);
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                MyApplication.this.onDisConnect();

            }
        };
        NetworkStateReceiver.registerObserver(netChangeObserver);
    }

    /**
     * 当前没有网络连接
     */
    public void onDisConnect()
    {
        networkAvailable = false;
        if (currentActivity != null)
        {
            currentActivity.onDisConnect();
        }
    }

    /**
     * 网络连接连接时调用
     */
    protected void onConnect(NetWorkUtil.netType type)
    {
        networkAvailable = true;
        if (currentActivity != null)
        {
            currentActivity.onConnect(type);
        }
    }
}
