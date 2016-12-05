package com.olsplus.balancemall.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.PageIndicator;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.home.bean.AdvertisementOut;
import com.olsplus.balancemall.app.home.bean.HomeProductResult;
import com.olsplus.balancemall.app.home.request.HomeRequestImpl;
import com.olsplus.balancemall.app.home.view.IHomeView;
import com.olsplus.balancemall.app.web.WebActivity;
import com.olsplus.balancemall.component.BannerPagerAdapter;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.app.home.view.HomeFunctionView;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class CmsHomeFragment extends BaseFragment implements IHomeView,HomeFunctionView.OnHomeFunctionClickListener {

    private ViewPager viewPager;
    private PageIndicator mIndicator;
    private LinearLayout cardsLinearLayout;
    private LayoutInflater mLayoutInflater;

    private HomeRequestImpl homeRequest;

    private List<AdvertisementOut> advertisementOutList = new ArrayList<AdvertisementOut>();

    private Runnable autoScroll = new Runnable() {
        @Override
        public void run() {
            int currPos = viewPager.getCurrentItem();
            viewPager.setCurrentItem(currPos >= advertisementOutList.size() - 1 ? 0 : currPos + 1);
        }
    };

    public static CmsHomeFragment getInstance() {
        CmsHomeFragment cmsHomeFragment = new CmsHomeFragment();
        return cmsHomeFragment;
    }


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mLayoutInflater = LayoutInflater.from(mActivity);
        viewPager = ((ViewPager) view.findViewById(R.id.view_pager_gallery));
        mIndicator = ((PageIndicator) view.findViewById(R.id.indicator));
        cardsLinearLayout = (LinearLayout) view.findViewById(R.id.template_cards);
        initData();
    }

    private void initData(){
        homeRequest = new HomeRequestImpl(mActivity);
        homeRequest.setView(this);
        homeRequest.getIndex();
    }

    /**
     * 刷新首页
     */
    public void refreshHome(){
        if(homeRequest!=null){
            homeRequest.getIndex();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_cms_root;
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showShort(mActivity,msg);
    }

    @Override
    public void showAdView(List<AdvertisementOut> ads) {
        BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(getActivity(), ads);
        viewPager.setAdapter(bannerPagerAdapter);
        mIndicator.setViewPager(viewPager);
        advertisementOutList = ads;
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startAutoScroll(false);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startAutoScroll(true);
                }
                return false;
            }
        });
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                startAutoScroll(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        startAutoScroll(true);
    }

    @Override
    public void showServiceView(ArrayList<HomeProductResult> homeProductResults) {

        int count = homeProductResults.size();
        for(int i = 0;i<count;i++){
            View homeFunctionRoot = mLayoutInflater.inflate(R.layout.home_cms_card_function, rootView, false);
            TextView titleTv = (TextView) homeFunctionRoot.findViewById(R.id.title);
            HomeFunctionView homeFunctionView = (HomeFunctionView) homeFunctionRoot.findViewById(R.id.home_functions_view);
            homeFunctionView.setOnHomeFunctionClickListener(this);
            HomeProductResult homeProductResult = homeProductResults.get(i);
            if(homeProductResult!=null){
                titleTv.setText(homeProductResult.getTitle());
                homeFunctionView.init(homeProductResult.getServices());
            }
            cardsLinearLayout.addView(homeFunctionRoot);
        }
    }

    /**
     * 轮播广告*
     */
    private void startAutoScroll(boolean bAuto) {
        viewPager.removeCallbacks(autoScroll);
        if (bAuto) {
            viewPager.postDelayed(autoScroll, 5000);
        }
    }

    @Override
    public void OnFunctionClick(String title,String url) {
        if(TextUtils.isEmpty(url)){
            return;
        }
        Intent newIntent = new Intent(getActivity(), WebActivity.class);
        newIntent.putExtra("url",url) ;
        newIntent.putExtra("title", title);
        getActivity().startActivity(newIntent);
//        Intent intent = new Intent(mActivity, CheckoutMainActivity.class);
//        intent.putExtra("sumit_order_parmas","order:{\"local_service_id\":1,\"time\":\"2016-09-15 08:30\",\"product\":\n" +
//                "[{\"id\":12,\"sku_info\":{\"sku_value\":\"%E7%BA\n" +
//                "%A2%E8%89%B2\",\"price\":200,\"count\":1}}]}}");
//        startActivity(intent);
    }
}
