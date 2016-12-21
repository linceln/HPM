package com.olsplus.balancemall.app.mine.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.app.mine.bean.MessageCenterInfo;
import com.olsplus.balancemall.app.mine.view.MessageViewHolder;
import com.olsplus.balancemall.core.db.MessageDBHelper;

public class MessageAdapter extends RecyclerArrayAdapter<MessageCenterInfo> {

    private OnMessageItemClickListener mOnItemClickListener;
//    private Realm realm;

    public interface OnMessageItemClickListener {
        void onItemClick(int position, BaseViewHolder holder);
    }

    public void setOnMessageItemClickListener(OnMessageItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MessageAdapter(Context context) {
        super(context);
//        this.realm = realm;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(parent);
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {

        if (isSaved(mObjects.get(position).getId())) {
            mObjects.get(position).setRead(true);
        }

//        MessageCenterInfo message = realm.where(MessageCenterInfo.class).equalTo("id", mObjects.get(position).getId()).findFirst();
//        if (message != null) {
//            // 如果当前这条信息已经保存在数据库中，则将它设置为已读
//            mObjects.get(position).setRead(true);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 当前点击设为已读
                mObjects.get(position).setRead(true);
                notifyDataSetChanged();


                if (!isSaved(mObjects.get(position).getId())) {
                    insertData(mObjects.get(position).getId());
                }

//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        MessageCenterInfo message = realm.where(MessageCenterInfo.class).equalTo("id", mObjects.get(position).getId()).findFirst();
//                        if (message == null) {
//                            realm.copyToRealm(mObjects.get(position));
//                        }
//                        notifyDataSetChanged();
//                    }
//                });

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, holder);
                }
            }
        });
        super.OnBindViewHolder(holder, position);
    }

    // 插入数据
    public void insertData(String id) {
        final MessageDBHelper messageDBHelper = new MessageDBHelper(getContext());
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        db.insert("messages", null, values);
    }

    // 查询数据
    public boolean isSaved(String id) {
        boolean isSaved = false;
        final MessageDBHelper messageDBHelper = new MessageDBHelper(getContext());
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();
        Cursor cursor = db.query("messages", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String queryId = cursor.getString(cursor.getColumnIndex("id"));
                if (id.equals(queryId)) {
                    isSaved = true;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return isSaved;
    }
}
