package com.olsplus.balancemall.app.mine.view;


import com.olsplus.balancemall.app.mine.bean.MySeesionEntity;
import com.olsplus.balancemall.core.util.BaseView;

public interface IShowMytoreView extends BaseView {

    void showError(String msg);

    void showMySeesionView(MySeesionEntity data);



}
