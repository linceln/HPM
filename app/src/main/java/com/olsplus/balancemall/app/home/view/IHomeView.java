package com.olsplus.balancemall.app.home.view;


import com.olsplus.balancemall.app.home.bean.AdvertisementOut;
import com.olsplus.balancemall.app.home.bean.HomeProductResult;
import com.olsplus.balancemall.core.util.BaseView;

import java.util.ArrayList;
import java.util.List;

public interface IHomeView extends BaseView {

    void showError(String msg);

    void showAdView(List<AdvertisementOut> ads);

    void showServiceView(ArrayList<HomeProductResult> homeProductResults);
}
