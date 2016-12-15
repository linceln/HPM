package com.olsplus.balancemall.app.mine.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.app.mine.bean.MessageCenterInfo;
import com.olsplus.balancemall.app.mine.view.MessageViewHolder;

public class MessageAdapter extends RecyclerArrayAdapter<MessageCenterInfo> {

    private OnMessageItemClickListener mOnItemClickListener;

    public interface OnMessageItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnMessageItemClickListener(OnMessageItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, holder);
                }
            }
        });
    }
}
