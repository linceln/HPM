package com.olsplus.balancemall.component.dialog;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.update.FileSizeEvent;
import com.olsplus.balancemall.core.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class DownloadDialog extends DialogFragment {

    private ProgressBar pb;
    private TextView tv;

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getProgress(final FileSizeEvent fileSize) {

        pb.setIndeterminate(false);
        pb.setMax(100);
        pb.setProgress(0);
        pb.setProgress((int) (fileSize.getCurrentLen() * 100 / fileSize.getContentLen()));
        tv.setText(StringUtil.formatStorage(fileSize.getCurrentLen()) + "/" + StringUtil.formatStorage(fileSize.getContentLen()));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.progress_download, null);
        pb = (ProgressBar) view.findViewById(R.id.pbDownload);
        pb.setIndeterminate(true);
        tv = (TextView) view.findViewById(R.id.tvProgressDownload);
        tv.setText("正在连接中，请稍候...");
        builder.setCustomTitle(view);

        return builder.create();
    }
}
