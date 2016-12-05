package com.olsplus.balancemall.app.province.request;


import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.app.province.bean.BuildingAddressEntity;

import java.util.List;

public interface ICityBuildingRequest {

    interface GetCityCallback {

        void onGetCitySuccess(List<AddressEntity> addressEntityList);

        void onGetCityFailed(String msg);

    }

    interface BuildCityCallback {

        void onBuildCitySuccess(List<BuildingAddressEntity> buildingAddressEntityList);

        void onBuildCityFailed(String msg);

    }


    interface UpdateBuildingCallback {

        void onUpdateBuildingSuccess();

        void onUpdateBuildingFailed(String msg);

    }

    void getCitys(boolean isRefresh);

    void getCitysBuilding(boolean isRefresh,String city_id);

    void bindCitysBuilding(String building_id);

    
}
