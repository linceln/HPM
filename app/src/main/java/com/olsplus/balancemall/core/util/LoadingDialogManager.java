package com.olsplus.balancemall.core.util;


import android.support.v4.app.FragmentActivity;

import com.olsplus.balancemall.component.dialog.ProgressDialogFragment;

public class LoadingDialogManager {

    private static ProgressDialogFragment fragment = new ProgressDialogFragment();

    public static void showLoading(FragmentActivity activity, String msg) {
        fragment.setMessage(msg);
        fragment.show(activity.getSupportFragmentManager(), "");
    }

    public static void dismissLoading() {
        if (fragment != null) {
            fragment.dismiss();
        }
    }
}
