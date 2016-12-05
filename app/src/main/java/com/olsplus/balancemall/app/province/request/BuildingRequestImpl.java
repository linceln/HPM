package com.olsplus.balancemall.app.province.request;


import android.content.Context;

import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.app.province.bean.BuildingAddressEntity;
import com.olsplus.balancemall.app.province.bussiness.BuildingBussiness;
import com.olsplus.balancemall.app.province.view.IShowBuildView;
import com.olsplus.balancemall.app.province.view.IShowCitysView;

import java.util.ArrayList;
import java.util.List;

public class BuildingRequestImpl implements ICityBuildingRequest{

    private Context context;

    private BuildingBussiness buildingBussiness;

    private IShowCitysView iShowCitysView;

    private IShowBuildView iShowBuildView;


    public BuildingRequestImpl(Context context) {
        this.context = context;
        buildingBussiness = new BuildingBussiness(context);
    }

    public void setShowCitysView(IShowCitysView iShowCitysView){
        this.iShowCitysView = iShowCitysView;
    }

    public void setShowBuildView(IShowBuildView iShowBuildView){
        this.iShowBuildView = iShowBuildView;
    }



    @Override
    public void getCitys(final boolean isRefresh) {
        buildingBussiness.getCity(new GetCityCallback() {
            @Override
            public void onGetCitySuccess(List<AddressEntity> addressEntityList) {
                ArrayList<Object> datas = new ArrayList<>();
                datas.add("热门城市");
                for(AddressEntity addressEntity :addressEntityList){
                    datas.add(addressEntity);
                }
                datas.add("北京、深圳等城市正在开通中");
                if(isRefresh){
                    iShowCitysView.refreshCity(datas);
                }else {
                    iShowCitysView.showCitysView(datas);
                }
            }

            @Override
            public void onGetCityFailed(String msg) {
                iShowCitysView.showError(msg);
            }
        });

    }

    @Override
    public void getCitysBuilding(final boolean isRefresh,String city_id) {
        buildingBussiness.buildingCity(city_id, new BuildCityCallback() {
            @Override
            public void onBuildCitySuccess(List<BuildingAddressEntity> buildingAddressEntityList) {
                if(isRefresh){
                    iShowBuildView.refresh(buildingAddressEntityList);
                }else {
                    iShowBuildView.showBuildView(buildingAddressEntityList);
                }
            }

            @Override
            public void onBuildCityFailed(String msg) {
                iShowBuildView.showError(msg);
            }
        });
    }

    @Override
    public void bindCitysBuilding(String building_id) {
        buildingBussiness.updateBuilding(building_id, new UpdateBuildingCallback() {
            @Override
            public void onUpdateBuildingSuccess() {
                if(iShowBuildView!=null){
                    iShowBuildView.bindBuildingView();
                }
            }

            @Override
            public void onUpdateBuildingFailed(String msg) {
                if(iShowBuildView!=null){
                    iShowBuildView.showError(msg);
                }
            }
        });
    }
}
