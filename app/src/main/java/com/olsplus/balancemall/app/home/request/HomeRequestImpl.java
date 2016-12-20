package com.olsplus.balancemall.app.home.request;


import android.content.Context;

import com.olsplus.balancemall.app.home.bean.AdvertisementOut;
import com.olsplus.balancemall.app.home.bean.Container;
import com.olsplus.balancemall.app.home.bean.HomeProductResult;
import com.olsplus.balancemall.app.home.bean.PromoteTopic;
import com.olsplus.balancemall.app.home.bussiness.HomeBusiness;
import com.olsplus.balancemall.app.home.view.IHomeView;
import com.olsplus.balancemall.core.util.ListUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeRequestImpl implements IHomeDataRequest{

    private Context context;

    private HomeBusiness homeBusiness;
    private IHomeView view;

    public HomeRequestImpl(Context context) {
        homeBusiness = new HomeBusiness(context);
    }

    public void setView(IHomeView view){
        this.view = view;
    }



    @Override
    public void getIndex() {
        if(homeBusiness !=null){
            homeBusiness.getHomeIndex(new HomeIndexCallback() {

                @Override
                public void onGetHomeSuccess(Container container) {
                    if(container!=null){
                        List<AdvertisementOut> ads = container.getAds();
                        if(ads!=null && !ads.isEmpty()){
                            if(view!=null){
                                view.showAdView(ads);
                            }
                        }
                        ArrayList<HomeProductResult> homeProductResults = new ArrayList<HomeProductResult>();
                        List<PromoteTopic> promoteTopicList = container.getServices();
                        Map<String ,List<PromoteTopic>> map =  ListUtil.group(promoteTopicList, new ListUtil.GroupBy<String>() {
                            @Override
                            public String groupby(Object obj) {
                                PromoteTopic promoteTopic = (PromoteTopic)obj;
                                return promoteTopic.getCategory_name();
                            }
                        });
                        if(map == null){
                            if(view!=null) {
                                view.showError("数据出错了");
                            }
                            return;
                        }
                        Iterator it = map.keySet().iterator();
                        while (it.hasNext()) {
                            HomeProductResult homeProductResult = new HomeProductResult();
                            String key = it.next().toString();
                            homeProductResult.setTitle(key);
                            homeProductResult.setServices(map.get(key));
                            homeProductResults.add(homeProductResult);
                        }
                        if(view!=null) {
                            view.showServiceView(homeProductResults);
                        }
                    }
                }

                @Override
                public void onGetHomeFailed(String msg) {
                    if(view!=null) {
                        view.showError(msg);
                    }
                }
            });
        }
    }
}
