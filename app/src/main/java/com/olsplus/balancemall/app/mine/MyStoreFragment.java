package com.olsplus.balancemall.app.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.merchant.MerchantActivity;
import com.olsplus.balancemall.app.mine.bean.MySeesionEntity;
import com.olsplus.balancemall.app.mine.bean.MyStoreOrderResult;
import com.olsplus.balancemall.app.mine.bean.UserInfo;
import com.olsplus.balancemall.app.mine.config.AboutNewActivity;
import com.olsplus.balancemall.app.mine.config.SettingActivity;
import com.olsplus.balancemall.app.mine.message.MyMessageActivity;
import com.olsplus.balancemall.app.mine.request.UserImpl;
import com.olsplus.balancemall.app.mine.view.IShowMytoreView;
import com.olsplus.balancemall.app.pay.voucher.MyCouponActivity;
import com.olsplus.balancemall.app.province.BuildCityActivity;
import com.olsplus.balancemall.component.view.CircleImageView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.ToastUtil;


public class MyStoreFragment extends BaseFragment implements View.OnClickListener, IShowMytoreView {

    private CircleImageView userPhoto;
    private TextView nickNameTv;
    private TextView pointTv;
    private TextView orderNumTv1;
    private TextView orderNumTv2;
    private TextView orderNumTv3;
    private TextView orderNumTv4;
    private LinearLayout couponLinear;
    private LinearLayout locationLinear;
    private TextView locationTv;
    private LinearLayout setttingLinear;
    private LinearLayout aboutLinear;

    private UserImpl userImpl;
    private TextView tvBusinessManage;

    public static MyStoreFragment getInstance() {
        MyStoreFragment myStoreFragment = new MyStoreFragment();
        return myStoreFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        // TODO 消息数量
//        messageCountTv = (TextView) view.findViewById(R.id.mystore_message_count_tv);
        userPhoto = (CircleImageView) view.findViewById(R.id.cover_user_photo);
//        userPhoto.setOnClickListener(this);
        nickNameTv = (TextView) view.findViewById(R.id.mystore_userinfo__nickname_txt);
        pointTv = (TextView) view.findViewById(R.id.mystore_userinfo_point_descr);
        view.findViewById(R.id.mystore_order_status1).setOnClickListener(this);
        view.findViewById(R.id.mystore_order_status2).setOnClickListener(this);
        view.findViewById(R.id.mystore_order_status3).setOnClickListener(this);
        view.findViewById(R.id.mystore_order_status4).setOnClickListener(this);
        orderNumTv1 = (TextView) view.findViewById(R.id.mystore_oder_numtv1);
        orderNumTv2 = (TextView) view.findViewById(R.id.mystore_oder_numtv2);
        orderNumTv3 = (TextView) view.findViewById(R.id.mystore_oder_numtv3);
        orderNumTv4 = (TextView) view.findViewById(R.id.mystore_oder_numtv4);
        couponLinear = (LinearLayout) view.findViewById(R.id.mystore_layout_userinfo_coupon);
        couponLinear.setOnClickListener(this);
        locationLinear = (LinearLayout) view.findViewById(R.id.mystore_layout_userinfo_location);
        locationTv = (TextView) view.findViewById(R.id.my_location_name);
        locationLinear.setOnClickListener(this);
        setttingLinear = (LinearLayout) view.findViewById(R.id.mystore_layout_userinfo_setting);
        setttingLinear.setOnClickListener(this);
        aboutLinear = (LinearLayout) view.findViewById(R.id.mystore_layout_userinfo_about);
        aboutLinear.setOnClickListener(this);
        // 商家管理
        tvBusinessManage = (TextView) view.findViewById(R.id.tvBusinessManage);
        tvBusinessManage.setOnClickListener(this);
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.mystore_fragment;
    }

    private void initData() {
        userImpl = new UserImpl(mActivity);
        userImpl.setIShowMytoreView(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (userImpl != null) {
            userImpl.getMySeesionData();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
//            case R.id.cover_user_photo:
            // TODO 查看图片
//                break;
            case R.id.mystore_order_status1:
                intent = new Intent(mActivity, MyOrderListActivity.class);
                intent.putExtra("current_item", 0);
                break;
            case R.id.mystore_order_status2:
                intent = new Intent(mActivity, MyOrderListActivity.class);
                intent.putExtra("current_item", 1);
                break;
            case R.id.mystore_order_status3:
                intent = new Intent(mActivity, MyOrderListActivity.class);
                intent.putExtra("current_item", 2);
                break;
            case R.id.mystore_order_status4:
                intent = new Intent(mActivity, MyOrderListActivity.class);
                intent.putExtra("current_item", 3);
                break;
            case R.id.mystore_layout_userinfo_location:
                intent = new Intent(mActivity, BuildCityActivity.class);
                intent.putExtra("city_id", "1");
                intent.putExtra("city_name", "厦门");
                intent.putExtra("from", "my");
                break;
            case R.id.mystore_layout_userinfo_setting:
                intent = new Intent(mActivity, SettingActivity.class);
                break;
            case R.id.mystore_layout_userinfo_about:
                intent = new Intent(mActivity, AboutNewActivity.class);
                break;
            case R.id.mystore_layout_userinfo_coupon:
                intent = new Intent(mActivity, MyCouponActivity.class);
                break;
            case R.id.right_operation_rl:
                intent = new Intent(mActivity, MyMessageActivity.class);
                break;
            case R.id.tvBusinessManage://商家管理
                intent = new Intent(mActivity, MerchantActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showShort(mActivity, msg);
    }

    @Override
    public void showMySeesionView(MySeesionEntity data) {
        UserInfo userInfo = data.getUserinfo();
        if (userInfo != null) {
            if (TextUtils.isEmpty(userInfo.getLocal_service_id())) {
                // 不显示商家管理
                tvBusinessManage.setVisibility(View.GONE);
            }
            String name = userInfo.getName();
            String photo = userInfo.getAvatar();
            String points = userInfo.getPoints();
            nickNameTv.setText(name);
            pointTv.setText("积分：" + points);
            Glide.with(mActivity)
                    .load(ApiConst.BASE_IMAGE_URL + photo)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(userPhoto);
            SPUtil.put(mActivity, SPUtil.AVATAR, photo);
            SPUtil.put(mActivity, SPUtil.GENDER, userInfo.getGender());
            SPUtil.put(mActivity, SPUtil.LOCAL_SERVICE_ID, userInfo.getLocal_service_id());
            SPUtil.put(mActivity, SPUtil.LOCAL_SERVICE_NAME, userInfo.getLocal_service_name());
            SPUtil.put(mActivity, SPUtil.POINTS_RULE, data.getPoint_rule());
            MyStoreOrderResult myOrderResult = data.getOrder_count();
            if (myOrderResult != null) {
                int waitForPay = myOrderResult.getWaitForPay();
                int waitForRemark = myOrderResult.getWaitForRemark();
                int refund = myOrderResult.getRefund();
                int total = waitForPay + waitForRemark + refund;
                if (total > 0) {
                    orderNumTv1.setVisibility(View.VISIBLE);
                    orderNumTv1.setText("" + total);
                } else {
                    orderNumTv1.setVisibility(View.GONE);
                }
                if (waitForPay > 0) {
                    orderNumTv2.setVisibility(View.VISIBLE);
                    orderNumTv2.setText("" + waitForPay);
                } else {
                    orderNumTv2.setVisibility(View.GONE);
                }
                if (waitForRemark > 0) {
                    orderNumTv3.setVisibility(View.VISIBLE);
                    orderNumTv3.setText("" + waitForRemark);
                } else {
                    orderNumTv3.setVisibility(View.GONE);
                }
                if (refund > 0) {
                    orderNumTv4.setVisibility(View.VISIBLE);
                    orderNumTv4.setText("" + refund);
                } else {
                    orderNumTv4.setVisibility(View.GONE);
                }

                locationTv.setText(userInfo.getBuilding_name());
                int messageCount = data.getMsgs();
                if (messageCount > 0) {
//                    messageCountTv.setVisibility(View.VISIBLE);
//                    messageCountTv.setText("" + messageCount);
                } else {
//                    messageCountTv.setVisibility(View.GONE);
                }
            }

        }
    }
}
