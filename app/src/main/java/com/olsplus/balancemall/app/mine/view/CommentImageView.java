package com.olsplus.balancemall.app.mine.view;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.olsplus.balancemall.R;

public class CommentImageView extends RelativeLayout {

    private RelativeLayout view;
    private ImageView img;
    private ImageView del;

    public CommentImageView(Context context) {
        super(context);
        this.view = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.comment_upload_img_layout, this);
        this.img = (ImageView)this.view.findViewById(R.id.product_img);
        this.del = (ImageView)this.view.findViewById(R.id.delete_img);
    }

    public void showImage(String pic){
        if(!TextUtils.isEmpty(pic)){
            Glide.with(getContext()).load(pic).into(img);
        }
    }

    public ImageView getDelView(){
        return del;
    }

}
