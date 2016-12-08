package com.olsplus.balancemall.component.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.olsplus.balancemall.R;

public class ProgressDialogFragment extends DialogFragment {


    private String message;

    public ProgressDialogFragment() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.progress_loading, null);
        TextView tvProgress = (TextView) view.findViewById(R.id.tvProgress);
        if (!TextUtils.isEmpty(message)) {
            tvProgress.setText(message);
        }
        builder.setCustomTitle(view);
        builder.setCancelable(false);
        return builder.create();
    }
}