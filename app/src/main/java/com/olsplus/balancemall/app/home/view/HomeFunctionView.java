package com.olsplus.balancemall.app.home.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.home.bean.PromoteTopic;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.ScreenUtil;

import java.util.List;

public class HomeFunctionView extends LinearLayout {

    private OnHomeFunctionClickListener onHomeFunctionClickListener;

    public interface OnHomeFunctionClickListener {
        public void OnFunctionClick(String title,String url);
    }


    public HomeFunctionView(Context context) {
        super(context);
    }

    public HomeFunctionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeFunctionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnHomeFunctionClickListener(OnHomeFunctionClickListener onHomeFunctionClickListener) {
        this.onHomeFunctionClickListener = onHomeFunctionClickListener;
    }


    public void init(List<PromoteTopic> homePromotionDetailList) {
        removeAllViews();
        initView(0, homePromotionDetailList);
//        initView(2,homePromotionDetailList);
    }

    private void initView(int index, List<PromoteTopic> homePromotionDetailList) {
        LinearLayout localLinearLayout = new LinearLayout(getContext());
        int count;
        count = homePromotionDetailList.size();

        int width = (ScreenUtil.getScreenWidth(getContext()) - getContext().getResources().getDimensionPixelOffset(R.dimen.rock_title_height)) / 2;
        LinearLayout containerLl = null;
        for (int j = index; j < count; j++) {
            if (j % 2 == 0) {
                containerLl = new LinearLayout(getContext());
                containerLl.setOrientation(LinearLayout.HORIZONTAL);
                addView(containerLl, new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }
            ViewGroup localViewGroup = (ViewGroup) inflate(getContext(), R.layout.home_cms_card_fuctions_item, null);
            ImageView imageView = (ImageView) localViewGroup.findViewById(R.id.module_icon);
            TextView moduleTv = (TextView) localViewGroup.findViewById(R.id.module_text);
            TextView moduleTipTv = (TextView) localViewGroup.findViewById(R.id.module_tips);
            final PromoteTopic homePromotionDetail = homePromotionDetailList.get(j);
            if (homePromotionDetail != null) {
                moduleTv.setText(homePromotionDetail.getProvider_name());
                moduleTipTv.setText(homePromotionDetail.getProvider_desc());
                String imageUrl = ApiConst.BASE_IMAGE_URL+ homePromotionDetail.getIcon();
                Glide.with(getContext())
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
                localViewGroup.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onHomeFunctionClickListener != null) {
                            String res_path = homePromotionDetail.getRes_path();
                            String local_service_id = homePromotionDetail.getLocal_service_id();
                            String per_page = homePromotionDetail.getPer_page();
                            String url =ApiConst.BASE_HTML_URL+res_path+"/"+local_service_id+"?"+"count="+per_page;
                            onHomeFunctionClickListener.OnFunctionClick(homePromotionDetail.getProvider_name(),url);
                        }
                    }
                });
            }

            LayoutParams layoutParams = new LayoutParams(
                    width,
                    LayoutParams.WRAP_CONTENT);
//            layoutParams.weight = 1.0F;
            int top = getContext().getResources().getDimensionPixelOffset(R.dimen.rock_home_description_line_margin_top);
            if (j % 2 == 0) {
                layoutParams.setMargins(0, top, getContext().getResources().getDimensionPixelOffset(R.dimen.rock_home_layout_padding_left), 0);
            } else {
                layoutParams.setMargins(getContext().getResources().getDimensionPixelOffset(R.dimen.rock_home_layout_padding_left), top, 0, 0);
            }
            containerLl.addView(localViewGroup, layoutParams);
        }
//        addView(localLinearLayout, new LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT));

    }


}
