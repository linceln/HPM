package com.olsplus.balancemall.core.image;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.core.util.ApiConst;

public class ImageHelper {

    public static void display(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(ApiConst.BASE_IMAGE_URL + url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void display(Activity activity, ImageView imageView, String url) {
        Glide.with(activity)
                .load(ApiConst.BASE_IMAGE_URL + url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void display(Fragment fragment, ImageView imageView, String url) {
        Glide.with(fragment)
                .load(ApiConst.BASE_IMAGE_URL + url)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void displayNoHolder(Activity activity, ImageView imageView, String url) {
        Glide.with(activity)
                .load(ApiConst.BASE_IMAGE_URL + url)
                .error(R.mipmap.ic_launcher)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 显示本地图片
     *
     * @param activity
     * @param imageView
     * @param path
     */
    public static void displayLocal(Activity activity, ImageView imageView, String path) {
        Glide.with(activity)
                .load(path)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

}
