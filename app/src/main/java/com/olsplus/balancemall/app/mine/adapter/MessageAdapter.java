package com.olsplus.balancemall.app.mine.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.app.mine.bean.MessageCenterInfo;
import com.olsplus.balancemall.app.mine.view.MessageViewHolder;

import io.realm.Realm;

public class MessageAdapter extends RecyclerArrayAdapter<MessageCenterInfo> {

    private OnMessageItemClickListener mOnItemClickListener;
    private Realm realm;

    public interface OnMessageItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnMessageItemClickListener(OnMessageItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MessageAdapter(Context context, Realm realm) {
        super(context);
        this.realm = realm;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {

        MessageCenterInfo message = realm.where(MessageCenterInfo.class).equalTo("id", mObjects.get(position).getId()).findFirst();
        if (message != null) {
            // 如果当前这条信息已经保存在数据库中，则将它设置为已读
            mObjects.get(position).setRead(true);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 当前点击设为已读
                mObjects.get(position).setRead(true);
                // 当前点击的信息保存到数据库
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        MessageCenterInfo message = realm.where(MessageCenterInfo.class).equalTo("id", mObjects.get(position).getId()).findFirst();
                        if (message == null) {
                            realm.copyToRealm(mObjects.get(position));
                        }
                        notifyDataSetChanged();
                    }
                });

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, holder);
                }
            }
        });
        super.OnBindViewHolder(holder, position);
    }
}
