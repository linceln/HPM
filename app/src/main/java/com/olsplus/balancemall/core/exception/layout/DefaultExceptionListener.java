package com.olsplus.balancemall.core.exception.layout;

import android.view.View;

import com.olsplus.balancemall.R;


public class DefaultExceptionListener implements ExceptionManager.OnExceptionListener {

    private View.OnClickListener listener;

    public DefaultExceptionListener(View.OnClickListener listener) {

        this.listener = listener;
    }

    @Override
    public void onRetry(View retryView) {
        retryView.findViewById(R.id.retry).setOnClickListener(listener);
    }

    @Override
    public void onEmpty(View emptyView) {
        emptyView.findViewById(R.id.empty).setOnClickListener(listener);
    }

    @Override
    public void onLoading(View loadingView) {
    }
}
