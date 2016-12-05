package com.olsplus.balancemall.app.province;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.province.adapter.CityAdapter;
import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.app.province.request.BuildingRequestImpl;
import com.olsplus.balancemall.app.province.view.IShowCitysView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.List;


public class ProvinceSwitchFragment extends BaseFragment implements IShowCitysView,SwipeRefreshLayout.OnRefreshListener,CityAdapter.OnCityItemClickListener {

    private EasyRecyclerView mCitysRecyclerView;
    private LinearLayout nullLinearLayout;
    private CityAdapter cityAdapter;
    private BuildingRequestImpl buildingRequest;
    private List<AddressEntity> addressEntitieDatas;

    public static ProvinceSwitchFragment getInstance() {
        ProvinceSwitchFragment provinceSwitchFragment = new ProvinceSwitchFragment();
        return provinceSwitchFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_province_fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        nullLinearLayout = (LinearLayout)view.findViewById(R.id.search_null_product_linear);
        mCitysRecyclerView = (EasyRecyclerView)view.findViewById(R.id.result_listview);
        mCitysRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.divider_line_color), DensityUtil.dp2px(mActivity, 0.5f), DensityUtil.dp2px(mActivity, 14f), 0);
//        mCitysRecyclerView.addItemDecoration(itemDecoration);
        cityAdapter = new CityAdapter(mActivity);
        cityAdapter.setOnCityItemClickListener(this);
        mCitysRecyclerView.setAdapterWithProgress(cityAdapter);
        mCitysRecyclerView.setRefreshListener(this);
        initData();
    }


    private void initData(){
        buildingRequest = new BuildingRequestImpl(mActivity);
        buildingRequest.setShowCitysView(this);
        buildingRequest.getCitys(false);
    }

    @Override
    public void onRefresh() {
        buildingRequest.getCitys(true);
    }

    @Override
    public void showError(String msg) {
        nullLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCitysView(List<Object> datas) {
        nullLinearLayout.setVisibility(View.GONE);
        cityAdapter.addAll(datas);
    }

    @Override
    public void refreshCity(List<Object> datas) {
        ToastUtil.showShort(mActivity,"刷新成功");
        nullLinearLayout.setVisibility(View.GONE);
        cityAdapter.clear();
        cityAdapter.addAll(datas);
    }

    @Override
    public void onItemClick(int position, BaseViewHolder holder) {
        List<Object> datas = cityAdapter.getAllData();
        if(datas!=null && !datas.isEmpty()){
            Object object = datas.get(position);
            if(object instanceof AddressEntity){
                AddressEntity addressEntity = (AddressEntity)object;
                Intent intent = new Intent(mActivity,BuildCityActivity.class);
                intent.putExtra("city_id",addressEntity.getId());
                intent.putExtra("city_name",addressEntity.getName());
                intent.putExtra("from","");
                startActivity(intent);
            }
        }

    }
}
