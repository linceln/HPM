
package com.olsplus.balancemall.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.olsplus.balancemall.R;

import static android.view.ViewGroup.*;


public class WarnDialog
{

    private Context context;
    private Dialog dialog;

    private TextView titleTv;
    private TextView textViewMsg;
    private TextView buttonCancel;
    private TextView buttonConfirm;

    private DialogInterface.OnClickListener cancelOnClickListener;
    private DialogInterface.OnClickListener confirmOnClickListener;

    public WarnDialog (Context context)
    {
        this.context = context;
//        init();
    }

    public Dialog init()
    {
       final Dialog dialog = new Dialog(context, R.style.Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_base, null);
        dialog.setContentView(view);
//        dialog = builder.create();
        titleTv = (TextView)view.findViewById(R.id.title);
        textViewMsg= (TextView)view.findViewById(R.id.message);
        buttonCancel = (TextView) view.findViewById(R.id.negativeButton);
        buttonConfirm = (TextView) view.findViewById(R.id.positiveButton);
        buttonCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOnClickListener.onClick(dialog,-2);
                dialog.dismiss();
            }
        });
        buttonConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOnClickListener.onClick(dialog,-1);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    public void setTitle(String title)
    {
        if(TextUtils.isEmpty(title)){
            titleTv.setVisibility(View.GONE);
        }else {
            titleTv.setText(title);
        }
    }


    public void setMessage(String msg)
    {
        if(TextUtils.isEmpty(msg)){
            textViewMsg.setVisibility(View.GONE);
        }else {
            textViewMsg.setText(msg);
        }
    }

    public void setNegative(String negative)
    {
        buttonCancel.setText(negative);
    }

    public void setPositive(String positive)
    {
        buttonConfirm.setText(positive);
    }

    public void setCancelable(boolean flag)
    {
        dialog.setCancelable(flag);
    }

    public void setOnCancelListener(DialogInterface.OnClickListener cancelOnClickListener)
    {
       this.cancelOnClickListener = cancelOnClickListener;
    }

    public void setOnPositiveListener(DialogInterface.OnClickListener confirmOnClickListener)
    {
        this.confirmOnClickListener = confirmOnClickListener;
    }

//    public void show()
//    {
//        if (dialog != null)
//        {
//            dialog.show();
//        }
//    }
//    public void dismissLoading()
//    {
//        if (dialog != null)
//        {
//            dialog.dismissLoading();
//        }
//    }
}
