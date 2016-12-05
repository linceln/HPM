package com.olsplus.balancemall.app.mystore.adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.mystore.bean.MyOrderInfo;
import com.olsplus.balancemall.app.mystore.view.CommentViewHolder;

import java.util.HashMap;
import java.util.Map;

public class CommentUploadAdapter extends RecyclerArrayAdapter<MyOrderInfo> {

    private Map<Integer,String> commentMap = new HashMap<Integer,String>();

    public CommentUploadAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(parent);
    }

    public Map<Integer, String> getCommentMap() {
        return commentMap;
    }

    public void setCommentMap(Map<Integer, String> commentMap) {
        this.commentMap = commentMap;
    }

    @Override
    public void OnBindViewHolder(final BaseViewHolder holder, final int position) {
        super.OnBindViewHolder(holder, position);
        final View divider = (View) holder.itemView.findViewById(R.id.divider_view);
        if(position == 0){
            divider.setVisibility(View.GONE);
        }else{
            divider.setVisibility(View.VISIBLE);
        }
        final EditText commentEd = (EditText) holder.itemView.findViewById(R.id.product_comment_edit);
        commentEd.setTag(position);
        commentEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int index = (Integer)commentEd.getTag();
                commentMap.put(index,commentEd.getText().toString().trim());
            }
        });
        if(!TextUtils.isEmpty(commentMap.get(position))){
            commentEd.setText(commentMap.get(position));
        }else{
            commentEd.setText("");
        }
    }
}
