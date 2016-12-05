package com.olsplus.balancemall.core.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.text.DecimalFormat;

public class UIUtil {

    private Activity mActivity;

//    private DialogHelper mDialogHelper;

    public UIUtil(Activity mActivity) {
        this.mActivity = mActivity;
//       this.mDialogHelper = new DialogHelper(mActivity);
    }

    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    public static String formatLablePrice(Double paramDouble) {
        return "¥ " + formatPrice(paramDouble);
    }

    public static Double formatPrice(Double paramDouble) {
        if (paramDouble == null) {
            return Double.valueOf(0.0D);
        }
        String str = String.valueOf(paramDouble);
        if ((str.lastIndexOf(".") != -1) && (str.substring(str.lastIndexOf(".") + 1, str.length()).length() > 1)) {
            return formatDefaultPrice(paramDouble, "0.00");
        }
        return formatDefaultPrice(paramDouble, "0.0");
    }

    private static Double formatDefaultPrice(Double paramDouble, String paramString) {
        if (paramDouble == null) {
            return Double.valueOf(0.0D);
        }
        return Double.valueOf(Double.parseDouble(new DecimalFormat(paramString).format(paramDouble)));
    }


    public static void setClearText(final TextView paramTextView, final View paramView) {
        paramView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paramTextView.setText("");
            }
        });
        if (!TextUtils.isEmpty(getText(paramTextView))) {
            paramView.setVisibility(View.VISIBLE);
        } else {
            paramView.setVisibility(View.INVISIBLE);
        }
        paramTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!TextUtils.isEmpty(getText(paramTextView))) {
                    paramView.setVisibility(View.VISIBLE);
                } else {
                    paramView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public static void addFontSpan(TextView tv, String str, int start, int length, int font) {
        SpannableString spanString = new SpannableString(str);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(font);
        spanString.setSpan(span, start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spanString);
    }

    public static void addColorSpan(TextView tv, String str, int length, String color) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
        spanString.setSpan(span, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spanString);
    }

    public static void addColorSpan(TextView tv, String str, int start, int length, String color) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor(color));
        spanString.setSpan(span, start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spanString);
    }

    public static Spannable getColorSpan(Context context, String str, int start, int length, @ColorRes int color) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(color));
        spanString.setSpan(span, start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    public static void addStrikeSpan(TextView tv, String str) {
        SpannableString spanString = new SpannableString(str);
        StrikethroughSpan span = new StrikethroughSpan();
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spanString);
    }


    public static void closeKeyBoard(View paramView) {
        InputMethodManager localInputMethodManager = (InputMethodManager) paramView.getContext().getSystemService("input_method");
        if (localInputMethodManager.isActive()) {
            localInputMethodManager.hideSoftInputFromWindow(paramView.getWindowToken(), 2);
        }
    }


//    public void showProgressDialog(String msg) {
//        mDialogHelper.showProgressDialog(msg,false,false);
//    }
//
//    public void dismissProgressDialog() {
//        mDialogHelper.dismissProgressDialog();
//    }
//
//    public void alert(String title, String msg, String positive,
//                      DialogInterface.OnClickListener positiveListener, String negative,
//                      DialogInterface.OnClickListener negativeListener) {
//        mDialogHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
//    }
//
//    public void showCustomDialog(String title, String msg, String positive,
//                      DialogInterface.OnClickListener positiveListener, String negative,
//                      DialogInterface.OnClickListener negativeListener) {
//        mDialogHelper.showCustomDialog(title, msg, positive, positiveListener, negative, negativeListener);
//    }
}
