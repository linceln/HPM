package com.olsplus.balancemall.app.mystore.view;


import com.olsplus.balancemall.app.mystore.bean.MySeesionEntity;
import com.olsplus.balancemall.core.util.BaseView;

public interface IShowMytoreView extends BaseView {

    void showError(String msg);

    void showMySeesionView(MySeesionEntity data);



}
