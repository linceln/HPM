package com.olsplus.balancemall.core.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.component.dialog.WarnDialog;


public class DialogHelper {
    private Activity    mActivity;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private Toast       mToast;


    public DialogHelper(Activity activity) {
        mActivity = activity;
    }

    /**
     *
     * @param title
     * @param message
     * @param positive
     * @param positiveListener
     * @param negative
     * @param negativeListener
     */
    public void showCustomDialog(String title,String message,String positive,DialogInterface.OnClickListener positiveListener,String negative,  DialogInterface.OnClickListener negativeListener){
        WarnDialog warnDialog = new WarnDialog(mActivity);
        Dialog dialog = warnDialog.init();
        warnDialog.setTitle(title);
        warnDialog.setMessage(message);
        warnDialog.setPositive(positive);
        warnDialog.setOnPositiveListener(positiveListener);
        warnDialog.setNegative(negative);
        warnDialog.setOnCancelListener(negativeListener);
        dialog.show();
    }

    /**
     * 弹对话框
     * 
     * @param title
     *            标题
     * @param msg
     *            消息
     * @param positive
     *            确定
     * @param positiveListener
     *            确定回调
     * @param negative
     *            否定
     * @param negativeListener
     *            否定回调
     */
    public void alert(final String title, final String msg, final String positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final String negative, final DialogInterface.OnClickListener negativeListener) {
        alert(title, msg, positive, positiveListener, negative, negativeListener, false);
    }

    /**
     * 弹对话框
     * 
     * @param title
     *            标题
     * @param msg
     *            消息
     * @param positive
     *            确定
     * @param positiveListener
     *            确定回调
     * @param negative
     *            否定
     * @param negativeListener
     *            否定回调
     * @param isCanceledOnTouchOutside
     *            是否可以点击外围框
     */
    public void alert(final String title, final String msg, final String positive,
                      final DialogInterface.OnClickListener positiveListener,
                      final String negative,
                      final DialogInterface.OnClickListener negativeListener,
                      final Boolean isCanceledOnTouchOutside) {
        dismissAlertDialog();

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                if (title != null) {
                    builder.setTitle(title);
                }
                if (msg != null) {
                    builder.setMessage(msg);
                }
                if (positive != null) {
                    builder.setPositiveButton(positive, positiveListener);
                }
                if (negative != null) {
                    builder.setNegativeButton(negative, negativeListener);
                }
                mAlertDialog = builder.show();
                mAlertDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
                mAlertDialog.setCancelable(false);
            }
        });
    }

    /**
     * TOAST
     * 
     * @param msg
     *            消息
     * @param period
     *            时长
     */
    public void toast(final String msg, final int period) {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
            }
        });
    }



    /**
     * 显示可取消的进度对话框
     * 
     */
    public void showProgressDialog( final String msg,final boolean cancelable,
                                   final boolean canceledOnTouchOutside) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }
                mProgressDialog = new ProgressDialog(mActivity, R.style.TheStoreWigdet_SherlockStyled_NoActionBar_Dialog);
                mProgressDialog.setMessage(msg);
                mProgressDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
                mProgressDialog.setCancelable(cancelable);
                mProgressDialog.show();
            }
        });
    }

    public  void dismissProgressDialog()
    {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null)
                {
                    mProgressDialog.dismiss();
                    mProgressDialog.cancel();
                }
            }
        });

    }

    public void dismissAlertDialog() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDialog != null && mAlertDialog.isShowing()) {
                    mAlertDialog.dismiss();
                    mAlertDialog = null;
                }
            }
        });
    }

}
