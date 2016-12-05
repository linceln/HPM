package com.olsplus.balancemall.app.province.view;

import com.olsplus.balancemall.app.province.bean.BuildingAddressEntity;
import com.olsplus.balancemall.core.util.BaseView;

import java.util.List;


public interface IShowBuildView extends BaseView {

    void showError(String msg);

    void showBuildView(List<BuildingAddressEntity> datas);

    void refresh(List<BuildingAddressEntity> datas);

    void bindBuildingView();

}
