package com.olsplus.balancemall.core.update;


import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.util.AppUtil;
import com.olsplus.balancemall.core.util.SPUtil;


public class UpdateUtil {

    private static final String APP_NAME = "";
    private static final String INFO_NAME = "";
    private static final String STORE_APK = "xiaobai";

    public static void checkUpdate(final Context context){
        long time = System.currentTimeMillis();
        SPUtil.put(context,SPUtil.UPDATE_TIME,time);
        String curVersion = AppUtil.getVersionName(context);
        Log.v("Update","cuversion is:"+curVersion);
        CheckUpdateBusiness.checkUpdate(new UpdateCallback() {
            @Override
            public void onSuccess(UpdateResult data) {

                Log.v("Update","update success");
                UpdateInfo updateInfo = data.getVersion();
                if(updateInfo!=null){
                    String version = updateInfo.getNumber();
                    if(!TextUtils.isEmpty(version)){
                        String valuse[] = version.split("\\.");
                        if(null != valuse && valuse.length>=2){
                            String currentVersion = AppUtil.getVersionName(context);
                            String currentValuse[] = currentVersion.split("\\.");
                            int one = Integer.parseInt(currentValuse[0]);
                            int two = Integer.parseInt(currentValuse[1]);
                            int three = Integer.parseInt(currentValuse[2]);

                            int one1 = Integer.parseInt(valuse[0]);
                            int two2 = Integer.parseInt(valuse[1]);
                            int three3 = Integer.parseInt(valuse[2]);
                            if(one1 > one){
                                showUpdateDialog(context,true,data.getVersion());
                            }
                            else if(two2 > two){
                                showUpdateDialog(context,false,data.getVersion());
                            }else if(two2 == two && three3>three){
                                showUpdateDialog(context,false,data.getVersion());
                            }
                        }
                    }
                }

            }

            @Override
            public void onError() {
                Log.v("Update","update error....");
            }
        });
    }

    private static void showUpdateDialog(final Context context,boolean isNeed, final UpdateInfo updateInfo){

        Log.v("Update","...show update dialog...");
        final Dialog dialog = new Dialog(context, R.style.Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.update_dialog, null);
        dialog.setContentView(view);
        TextView titleTv= (TextView)view.findViewById(R.id.update_title);
        TextView textViewMsg= (TextView)view.findViewById(R.id.update_content);
        Button buttonCancel = (Button) view.findViewById(R.id.update_id_cancel);
        Button buttonConfirm = (Button) view.findViewById(R.id.update_id_ok);
        titleTv.setText("版本更新");
        textViewMsg.setText(updateInfo.getInfo());
        if(isNeed){
            buttonCancel.setVisibility(View.GONE);
        }else {
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
                Log.v("Update","download click....");
                CheckUpdateBusiness.downloadApk(context,updateInfo,INFO_NAME,STORE_APK);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
