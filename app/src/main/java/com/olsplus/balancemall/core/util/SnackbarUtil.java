package com.olsplus.balancemall.core.util;


import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtil {

    public static void showShort(View v, String text) {
        Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show();
    }
}
