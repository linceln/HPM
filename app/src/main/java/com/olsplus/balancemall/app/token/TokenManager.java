package com.olsplus.balancemall.app.token;


import com.olsplus.balancemall.core.app.MyApplication;
import com.olsplus.balancemall.core.event.TokenEvent;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;

import rx.Subscriber;

public class TokenManager {

    private LinkedList<TokenTask> tokenTasks;

    private static TokenManager tokenManager = new TokenManager();

    private TokenManager() {
        tokenTasks = new LinkedList<>();
    }

    public static TokenManager getInstance() {
        return tokenManager;
    }

    public boolean hasTask() {
        return tokenTasks.isEmpty();
    }

    public void addTask(TokenTask tokenTask) {
        if (tokenTasks.isEmpty()) {
            tokenTasks.add(tokenTask);
        }
    }

    public void clear() {
        tokenTasks.clear();
    }

    public static void requestUpdateToken(final HttpResultObserver httpResultObserver) {
        final TokenManager tokenManager = TokenManager.getInstance();
        if (tokenManager != null) {
            if (tokenManager.hasTask()) {
                TokenTask tokenTask = new TokenTask();
                tokenManager.addTask(tokenTask);
                tokenTask.updateToken(new Subscriber<TokenResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(tokenManager);
                    }

                    @Override
                    public void onNext(TokenResult tokenResult) {
                        if (tokenResult == null) {
                            handleError(tokenManager);
                            return;
                        }
                        if (tokenResult.getRet() == 0) {
                            SPUtil.put(MyApplication.getApp(), SPUtil.TOKEN, tokenResult.getToken());
                            tokenManager.clear();
                            httpResultObserver.onReconnect();
                        } else {
                            handleError(tokenManager);
                        }
                    }
                });
            }
        }
    }

    private static void handleError(TokenManager tokenManager) {
        tokenManager.clear();
        ToastUtil.showShort(MyApplication.getApp(), "登录过期，请重新登录");
        EventBus.getDefault().post(new TokenEvent());
    }
}
