package com.olsplus.balancemall.core.update;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.component.dialog.UpgradeForceDialog;
import com.olsplus.balancemall.component.dialog.UpgradeNormalDialog;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;


public class UpgradeUtil {

    private static final String APP_NAME = "";
    private static final String INFO_NAME = "";
    private static final String STORE_APK = "HePingMao";

    public static void checkUpdate(final Context context) {
        // 保存检查更新时间
        SPUtil.put(context, SPUtil.UPDATE_TIME, System.currentTimeMillis());


        CheckUpdateBusiness.checkUpdate(new UpdateCallback() {
            @Override
            public void onSuccess(UpdateResult data) {

                UpdateInfo version = data.getVersion();
                if (version != null) {
                    // 当前版本
                    LogUtil.e("Update", "Local Version: v" + AppUtil.getVersionName(context));
                    // 云端版本
                    LogUtil.e("Update", "Cloud Version: v" + data.getVersion().getNumber());

                    String versionNum = version.getNumber();
                    if (!TextUtils.isEmpty(versionNum)) {
                        String cloudValues[] = versionNum.split("\\.");
                        if (null != cloudValues && cloudValues.length >= 2) {
                            String currentVersion = AppUtil.getVersionName(context);
                            String currentValues[] = currentVersion.split("\\.");
                            int one = Integer.parseInt(currentValues[0]);
                            int two = Integer.parseInt(currentValues[1]);
                            int three = Integer.parseInt(currentValues[2]);

                            int one1 = Integer.parseInt(cloudValues[0]);
                            int two2 = Integer.parseInt(cloudValues[1]);
                            int three3 = Integer.parseInt(cloudValues[2]);
                            if (one1 > one) {
                                // 大版本变化强制更新
//                                showUpdateDialog(context, true, data.getVersion());
                                upgradeForce(context);
                            } else if (two2 > two) {
                                // 小版本变化选择更新
//                                showUpdateDialog(context, false, data.getVersion());
                                upgradeNormal(context);
                            } else if (two2 == two && three3 > three) {
//                                showUpdateDialog(context, false, data.getVersion());
                                upgradeNormal(context);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError() {
                LogUtil.e("Update", "update error....");
            }
        });
    }

    /**
     * 选择更新
     *
     * @param context
     */
    private static void upgradeNormal(final Context context) {
        final UpgradeNormalDialog upgradeNormalDialog = new UpgradeNormalDialog();
        upgradeNormalDialog.setMessage(context.getString(R.string.upgrade_normal_msg));
        upgradeNormalDialog.setOnPositiveClickListener(new UpgradeNormalDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                upgradeNormalDialog.dismiss();
                toMarket(context);
            }
        });
    }

    /**
     * 强制更新
     *
     * @param context
     */
    private static void upgradeForce(final Context context) {
        final UpgradeForceDialog generalDialogFragment = new UpgradeForceDialog();
        generalDialogFragment.setMessage(context.getString(R.string.upgrade_force_msg));
        generalDialogFragment.setCancelable(false);
        generalDialogFragment.setOnPositiveClickListener(new UpgradeForceDialog.OnPositiveClickListener() {
            @Override
            public void onClick() {
                toMarket(context);
            }
        });
        generalDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "update");
    }

    /**
     * 跳转到应用市场详情页
     *
     * @param context
     */
    public static void toMarket(Context context) {
        //这里开始执行一个应用市场跳转逻辑
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        //存在手机里没安装应用市场的情况，跳转会报异常，做一个接收判断
        if (intent.resolveActivity(context.getPackageManager()) != null) { //可以接收
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "您的系统中没有安装应用市场", Toast.LENGTH_SHORT).show();
        }
    }

    private static void showUpdateDialog(final Context context, boolean isNeed, final UpdateInfo updateInfo) {

        LogUtil.e("Update", "...show update dialog...");
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.update_dialog, null);
        dialog.setContentView(view);
        TextView titleTv = (TextView) view.findViewById(R.id.update_title);
        TextView textViewMsg = (TextView) view.findViewById(R.id.update_content);
        Button buttonCancel = (Button) view.findViewById(R.id.update_id_cancel);
        Button buttonConfirm = (Button) view.findViewById(R.id.update_id_ok);
        titleTv.setText("版本更新");
        textViewMsg.setText(updateInfo.getInfo());
        if (isNeed) {
            buttonCancel.setVisibility(View.GONE);
        } else {
            buttonCancel.setVisibility(View.VISIBLE);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.e("Update", "download click....");
                CheckUpdateBusiness.downloadApk(context, updateInfo, INFO_NAME, STORE_APK);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


}
