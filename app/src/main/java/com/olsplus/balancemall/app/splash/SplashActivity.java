package com.olsplus.balancemall.app.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.login.LoginActivity;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class SplashActivity extends RxAppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        toHome();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toHome() {
        // 申请权限
        new RxPermissions(this)
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .flatMap(new Func1<Permission, Observable<Long>>() {
                    @Override
                    public Observable<Long> call(Permission permission) {
                        // 延时启动
                        return Observable.timer(1, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        // 跳转
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                    }
                });
    }
}
