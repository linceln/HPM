package com.olsplus.balancemall.core.exception.layout;

import android.view.View;
import android.view.ViewGroup;

import com.olsplus.balancemall.R;

public class ExceptionManager {

    private final ExceptionLayout exceptionLayout;

    private ExceptionManager(View view, OnExceptionListener listener) {
        ViewGroup parent = (ViewGroup) view.getParent();
        int index = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) == view) {
                // 找到view在parent中的位置
                index = i;
                break;
            }
        }

        ViewGroup.LayoutParams lp = view.getLayoutParams();

        ExceptionLayout exceptionLayout = new ExceptionLayout(view.getContext());
        // 在原来的view之前插入ExceptionLayout
        parent.removeView(view);
        parent.addView(exceptionLayout, index, lp);
        exceptionLayout.setContentView(view);

        exceptionLayout.setLoadingView(R.layout.base_loading);
        exceptionLayout.setEmptyView(R.layout.base_empty);
        exceptionLayout.setRetryView(R.layout.base_retry);
        exceptionLayout.showLoading();

        if (listener != null) {
            listener.onEmpty(exceptionLayout.getEmptyView());
            listener.onLoading(exceptionLayout.getLoadingView());
            listener.onRetry(exceptionLayout.getRetryView());
        }

        this.exceptionLayout = exceptionLayout;
    }

    public interface OnExceptionListener {
        void onRetry(View retryView);

        void onEmpty(View emptyView);

        void onLoading(View loadingView);
    }

    public static ExceptionManager initialize(View view, OnExceptionListener listener) {
        return new ExceptionManager(view, listener);
    }

    public void showEmpty() {
        exceptionLayout.showEmpty();
    }

    public void showRetry() {
        exceptionLayout.showRetry();
    }

    public void showLoading() {
        exceptionLayout.showLoading();
    }

    public void hide() {
        exceptionLayout.showContent();
    }
}
