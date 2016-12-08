package com.olsplus.balancemall.component.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class UpgradeForceDialog extends DialogFragment {

    private String msg;

    private OnPositiveClickListener listener;

    public void setMessage(String msg) {

        this.msg = msg;
    }

    public void setOnPositiveClickListener(OnPositiveClickListener listener) {

        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg)
//                .setCancelable(false)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        if (listener != null) {
                            listener.onClick();
                        }
                    }
                });
        return builder.create();
    }

    public interface OnPositiveClickListener {

        public void onClick();
    }
}