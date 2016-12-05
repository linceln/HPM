package com.olsplus.balancemall.component.recycler;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.RawRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.olsplus.balancemall.core.image.ImageHelper;


public class CustomViewHolder extends RecyclerView.ViewHolder {

    public CustomViewHolder(View itemView) {
        super(itemView);
    }

    public void setImage(Context context, int viewId, String url) {
        ImageView img = (ImageView) itemView.findViewById(viewId);
        ImageHelper.display((Activity) context, img, url);
    }

    public void setText(int viewId, String text) {

        TextView textView = (TextView) itemView.findViewById(viewId);
        textView.setText(text);
    }

    public void setOnClickListener(int viewId, View.OnClickListener listener) {

        View view = itemView.findViewById(viewId);
        view.setOnClickListener(listener);
    }

    public void setVisibility(int viewId, int visibility) {

        itemView.findViewById(viewId).setVisibility(visibility);
    }

    public void setRating(@IdRes int viewId, float rating) {

        ((RatingBar) itemView.findViewById(viewId)).setRating(rating);
    }

    public View getView(@IdRes int resId) {

        return itemView.findViewById(resId);
    }

    public String getText(@IdRes int resId) {
        TextView textView = (TextView) itemView.findViewById(resId);
        return textView.getText().toString().trim();
    }

    public void setTextWatcher(@IdRes int resId, TextWatcher watcher) {
        EditText editText = (EditText) itemView.findViewById(resId);
        editText.addTextChangedListener(watcher);
    }
}
