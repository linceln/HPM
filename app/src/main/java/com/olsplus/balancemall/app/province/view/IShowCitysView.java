package com.olsplus.balancemall.app.province.view;


import com.olsplus.balancemall.core.util.BaseView;

import java.util.List;

public interface IShowCitysView extends BaseView {

    void showError(String msg);

    void showCitysView(List<Object> datas);

    void refreshCity(List<Object> datas);

}
