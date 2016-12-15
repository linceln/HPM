package com.olsplus.balancemall.app.bottom;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.bottom.request.BottomImpl;
import com.olsplus.balancemall.app.bottom.view.IBottomView;
import com.olsplus.balancemall.app.cart.CartActivity;
import com.olsplus.balancemall.app.home.HomeActivity;
import com.olsplus.balancemall.app.mine.MyStoreActivity;
import com.olsplus.balancemall.core.app.BaseFragment;

public class BottomNavigateFragment extends BaseFragment implements IBottomView, View.OnClickListener{

    private ViewGroup rootView;
//    private List<IconMenuVO> iconMenuVOs;
    private LinearLayout menuLinear;
    private LinearLayout homeLinear;
    private LinearLayout cartLinear;
    private LinearLayout mystoreLinear;
    private ImageView homeIcon;
    private ImageView cartIcon;
    private TextView cartText;
    private ImageView mystoreIcon;
    private TextView mystoreText;
    private TextView cartNumTip;

    private BottomImpl bottomImpl;

    private String tag;

    public BottomNavigateFragment() {
        super();
        this.tag = null;
//        this.iconMenuVOs = new ArrayList<IconMenuVO>();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomImpl = new BottomImpl();
        bottomImpl.setView(this);

//        IconMenuVO v0 = new IconMenuVO();
//        v0.setName("生活服务");
//        v0.setUrl("xb://home");
//        v0.setDefaultIconResId(R.drawable.navigation_homebutton);
//        v0.setRedPoint(0);
//        v0.setShowWord(0);
//        this.iconMenuVOs.add(v0);
//        v0 = new IconMenuVO();
//        v0.setUrl("xb://cart");
//        v0.setName("购物车");
//        v0.setDefaultIconResId(R.drawable.navigation_cartbutton);
//        v0.setRedPoint(1);
//        v0.setShowWord(1);
//        this.iconMenuVOs.add(v0);
//        v0 = new IconMenuVO();
//        v0.setName("我的");
//        v0.setUrl("xb://mystore");
//        v0.setDefaultIconResId(R.drawable.navigation_mystorebutton);
//        v0.setRedPoint(0);
//        v0.setShowWord(1);
//        this.iconMenuVOs.add(v0);

    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        rootView = (ViewGroup) view;
        Intent intent = mActivity.getIntent();
        if(intent !=null){
            tag = intent.getStringExtra("dispatch_url");
        }
        if(TextUtils.isEmpty(tag)){
            tag = "xb://home";
        }
        menuLinear = (LinearLayout) view.findViewById(R.id.menu_root);
        homeLinear = (LinearLayout) view.findViewById(R.id.menu_home_layout);
        homeIcon = (ImageView) view.findViewById(R.id.menu_home_icon);
        cartLinear = (LinearLayout) view.findViewById(R.id.menu_cart_layout);
        cartIcon = (ImageView) view.findViewById(R.id.menu_cart_icon);
        cartText = (TextView) view.findViewById(R.id.menu_cart_text);
        cartNumTip= (TextView) view.findViewById(R.id.tips);
        mystoreLinear = (LinearLayout) view.findViewById(R.id.menu_my_layout);
        mystoreIcon = (ImageView) view.findViewById(R.id.menu_my_icon);
        mystoreText = (TextView) view.findViewById(R.id.menu_my_text);
        homeLinear.setOnClickListener(this);
        cartLinear.setOnClickListener(this);
        mystoreLinear.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.res_main_ui_bottom;
    }

    @Override
    public void showCartNum() {

    }

    /**
     * 显示底部菜单
     */
    @Override
    public void showBottomView() {
          if(tag.equals("xb://home")){
              homeIcon.setSelected(true);
              cartIcon.setSelected(false);
              cartText.setSelected(false);
              mystoreIcon.setSelected(false);
              mystoreText.setSelected(false);
          }
          else if(tag.equals("xb://cart")){
              homeIcon.setSelected(false);
              cartIcon.setSelected(true);
              cartText.setSelected(true);
              mystoreIcon.setSelected(false);
              mystoreText.setSelected(false);
          }
          else if(tag.equals("xb://mystore")){
              homeIcon.setSelected(false);
              cartIcon.setSelected(false);
              cartText.setSelected(false);
              mystoreIcon.setSelected(true);
              mystoreText.setSelected(true);
          }
//        int count = iconMenuVOs.size();
//        menuLinear.removeAllViews();
//        for (int i = 0; i < count; i++) {
//            IconMenuVO iconMenuVO = iconMenuVOs.get(i);
//            View item = LayoutInflater.from(mActivity).inflate(R.layout.res_main_ui_bottom_item, rootView, false);
//            LinearLayout itemLinear = (LinearLayout) item.findViewById(R.id.menu_layout);
//            itemLinear.setOnClickListener(this);
//            item.setTag(i);
//            ImageView icon = (ImageView) item.findViewById(R.id.menu_icon);
//            TextView text = (TextView) item.findViewById(R.id.menu_text);
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)text.getLayoutParams();
//            if (iconMenuVO != null) {
//                if(iconMenuVO.getShowWord() == 1){
//                    text.setTextOnCounting(iconMenuVO.getName());
//                }
//                if (iconMenuVO.getDefaultIconResId() != 0) {
//                    icon.setImageResource(iconMenuVO.getDefaultIconResId());
//                }
//                if (iconMenuVO.getUrl().startsWith(tag)) {
//                    icon.setSelected(true);
//                    text.setSelected(true);
//                } else {
//                    icon.setSelected(false);
//                    text.setSelected(false);
//                }
//                TextView tipTv = (TextView) item.findViewWithTag("tips_cart_number");
//                if (iconMenuVO.getUrl().startsWith("xb://cart")) {
//                    cartNumTip = tipTv;
//                    tipTv.setVisibility(View.INVISIBLE);
//                } else {
//                    tipTv.setVisibility(View.GONE);
//                }
//                if(i == 0){
//                    layoutParams.weight = 3;
//                }
//            }
//            menuLinear.addView(item);
//        }
    }
    @Override
    public void onResume() {
        super.onResume();
        bottomImpl.initBottomMenuView();
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (this.tag != null) {
            switch (v.getId()) {
                case R.id.menu_home_layout:
                    if (!tag.contains("home")) {
                        intent = new Intent(getActivity(), HomeActivity.class);
                        intent.putExtra("dispatch_url", "xb://home");
                    }
                    break;
                case R.id.menu_cart_layout:
                    if (!tag.contains("cart")) {
                        intent = new Intent(getActivity(), CartActivity.class);
                        intent.putExtra("dispatch_url", "xb://cart");
                    }
                    break;
                case R.id.menu_my_layout:
                    if (!tag.contains("mystore")) {
                        intent = new Intent(getActivity(), MyStoreActivity.class);
                        intent.putExtra("dispatch_url", "xb://mystore");
                    }
                    break;
            }
            if(intent!=null){
                this.startActivity(intent);
                this.getActivity().overridePendingTransition(0, 0);
            }

        }

    }
}
