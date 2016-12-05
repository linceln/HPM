package com.olsplus.balancemall.app.province;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.olsplus.balancemall.R;
import com.olsplus.balancemall.app.home.HomeActivity;
import com.olsplus.balancemall.app.province.adapter.BuildCityAdapter;
import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.app.province.bean.BuildingAddressEntity;
import com.olsplus.balancemall.app.province.request.BuildingRequestImpl;
import com.olsplus.balancemall.app.province.view.IShowBuildView;
import com.olsplus.balancemall.app.province.view.IShowCitysView;
import com.olsplus.balancemall.core.app.BaseFragment;
import com.olsplus.balancemall.core.util.DensityUtil;
import com.olsplus.balancemall.core.util.SPUtil;
import com.olsplus.balancemall.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class BuildCityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IShowBuildView,IShowCitysView,BuildCityAdapter.OnBuildingItemClickListener {

    private EasyRecyclerView mBuildCityRecyclerView;
    private BuildCityAdapter buildCityAdapter;
    private BuildingRequestImpl buildingRequest;
    private String cityId;
    private String from;
    private BuildCityActivity buildCityActivity;
    private List<AddressEntity> addressEntityList = new ArrayList<AddressEntity>();

    public static BuildCityFragment getInstance(String cityId,String from) {
        BuildCityFragment buildCityFragment = new BuildCityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city_id", cityId);
        bundle.putString("from", from);
        buildCityFragment.setArguments(bundle);
        return buildCityFragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        buildCityActivity = (BuildCityActivity) getActivity();
        mBuildCityRecyclerView = (EasyRecyclerView) view.findViewById(R.id.build_city_listview);
        mBuildCityRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerDecoration itemDecoration = new DividerDecoration(getResources().getColor(R.color.divider_line_color), DensityUtil.dp2px(mActivity, 0.5f), DensityUtil.dp2px(mActivity, 14f), 0);
        mBuildCityRecyclerView.addItemDecoration(itemDecoration);
        mBuildCityRecyclerView.setErrorView(R.layout.error_layout);
        buildCityAdapter = new BuildCityAdapter(mActivity);
        buildCityAdapter.setOnBuildingItemClickListener(this);
        mBuildCityRecyclerView.setAdapterWithProgress(buildCityAdapter);
        mBuildCityRecyclerView.setRefreshListener(this);
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            cityId = bundle.getString("city_id");
            from = bundle.getString("from");
            if(TextUtils.isEmpty(cityId)){
                mBuildCityRecyclerView.showError();
                return;
            }
        }
        buildingRequest = new BuildingRequestImpl(mActivity);
        buildingRequest.setShowBuildView(this);
        buildingRequest.setShowCitysView(this);
        buildingRequest.getCitysBuilding(false, cityId);
        buildingRequest.getCitys(false);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.home_build_city_fragment;
    }

    public  List<AddressEntity> getAllCitys(){
        return addressEntityList;
    }

    @Override
    public void onRefresh() {
        buildingRequest.getCitysBuilding(true, cityId);
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showShort(mActivity, msg);
        mBuildCityRecyclerView.showError();
    }

    @Override
    public void showCitysView(List<Object> datas) {
        int size = datas.size();
        addressEntityList.clear();;
        if(size>2){
            for(int i = 0;i<datas.size();i++){
                Object object = datas.get(i);
                if(object instanceof AddressEntity){
                    AddressEntity addressEntity = (AddressEntity)object;
                    addressEntityList.add(addressEntity);
                }
            }
        }
    }

    @Override
    public void refreshCity(List<Object> datas) {

    }


    @Override
    public void showBuildView(List<BuildingAddressEntity> datas) {
        buildCityAdapter.addAll(datas);
    }

    @Override
    public void refresh(List<BuildingAddressEntity> datas) {
        buildCityAdapter.clear();
        buildCityAdapter.addAll(datas);
    }

    @Override
    public void bindBuildingView() {
        SPUtil.put(mActivity,SPUtil.CITY_NAME,buildCityActivity.getCurrentCity());
        SPUtil.put(mActivity,SPUtil.CITY_ID,cityId);
        if(TextUtils.isEmpty(from)){
            Intent intent = new Intent(mActivity, HomeActivity.class);
            intent.putExtra("dispatch_url","xb://home");
            intent.putExtra("from","0");
            startActivity(intent);
        }

        mActivity.finish();
    }

    @Override
    public void onItemClick(int position, BaseViewHolder holder) {
        List<BuildingAddressEntity> datas = buildCityAdapter.getAllData();
        if(datas!=null && !datas.isEmpty()){
            BuildingAddressEntity data = datas.get(position);
            if(data!=null){
                String building_id = data.getId();
                buildingRequest.bindCitysBuilding(building_id);
            }
        }
    }
}
