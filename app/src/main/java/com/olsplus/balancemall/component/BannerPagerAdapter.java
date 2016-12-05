package com.olsplus.balancemall.component;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.home.bean.AdvertisementOut;
import com.olsplus.balancemall.app.web.WebActivity;
import com.olsplus.balancemall.core.util.ApiConst;

import java.util.List;

public class BannerPagerAdapter<T> extends BasePagerAdapter {
    private Context context;

    public BannerPagerAdapter(Context context, List<T> paramList) {
        super(paramList);
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = getRealPosition(position);
        if (viewCache != null && !viewCache.isEmpty()) {
            View view = View.inflate(context, R.layout.layout_home_cms_card_gallery_item, null);
            ImageView galleryImage = (ImageView) view.findViewById(R.id.gallery_img);
            final AdvertisementOut advertisementOut = (AdvertisementOut)viewCache.get(position);
            if(advertisementOut!=null){
                String imageUrl = ApiConst.BASE_IMAGE_URL+advertisementOut.getImg();
                Glide.with(context)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(galleryImage);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, WebActivity.class);
                        intent.putExtra("url",advertisementOut.getUrl());
                        context.startActivity(intent);
                    }
                });
            }
            container.addView(view);
            return view;
        }
        return super.instantiateItem(container, position);
    }
}
