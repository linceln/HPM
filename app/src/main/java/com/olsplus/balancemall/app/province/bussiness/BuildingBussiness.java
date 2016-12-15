package com.olsplus.balancemall.app.province.bussiness;


import android.content.Context;

import com.olsplus.balancemall.app.province.bean.AddressEntity;
import com.olsplus.balancemall.app.province.bean.BuildingAddressEntity;
import com.olsplus.balancemall.app.province.bean.BuildingResultEntity;
import com.olsplus.balancemall.app.province.bean.CityResultEntity;
import com.olsplus.balancemall.app.province.bean.UpdateBuildResultEntity;
import com.olsplus.balancemall.app.province.request.CityBuildingService;
import com.olsplus.balancemall.app.province.request.ICityBuildingRequest;
import com.olsplus.balancemall.core.http.HttpManager;
import com.olsplus.balancemall.core.http.HttpResultObserver;
import com.olsplus.balancemall.core.http.HttpUtil;
import com.olsplus.balancemall.core.util.ApiConst;
import com.olsplus.balancemall.core.util.DateUtil;
import com.olsplus.balancemall.core.util.LogUtil;
import com.olsplus.balancemall.core.util.SPUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BuildingBussiness {

    private Context context;

    public BuildingBussiness(Context context) {
        this.context = context;
    }

    /**
     * 获取城市列表
     *
     * @param callback
     */
    public void getCity(final ICityBuildingRequest.GetCityCallback callback) {
        String url = ApiConst.BASE_URL + "v1/city";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseCitySign(url, uid, token,timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<CityResultEntity>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                getCity(callback);
            }

            @Override
            public void onSuccess(CityResultEntity data) {
                if (data == null) {
                    callback.onGetCityFailed("数据出错了");
                    return;
                }
                List<AddressEntity> addressEntityList = data.getCities();
                if (addressEntityList != null && !addressEntityList.isEmpty()) {
                    callback.onGetCitySuccess(addressEntityList);
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("getcity", "getcity failed");
                callback.onGetCityFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(CityBuildingService.class)
                .getCitys(uid, token, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 获取指定城市的写字楼列表数据
     *
     * @param city_id
     * @param callback
     */
    public void buildingCity(final String city_id, final ICityBuildingRequest.BuildCityCallback callback) {
        String url = ApiConst.BASE_URL + "v1/building";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseBuildCitySign(city_id, url, uid, token,timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<BuildingResultEntity>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                buildingCity(city_id, callback);
            }

            @Override
            public void onSuccess(BuildingResultEntity data) {
                if (data == null) {
                    callback.onBuildCityFailed("数据出错了");
                    return;
                }
                List<BuildingAddressEntity> buildingAddressEntityList = data.getBuildings();
                if (buildingAddressEntityList != null ) {
                    callback.onBuildCitySuccess(buildingAddressEntityList);
                }
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("yongyuan,w", "buildcity failed");
                callback.onBuildCityFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(CityBuildingService.class)
                .buildCity(city_id, uid, token, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }


    /**
     * 设置所在的写字楼
     *
     * @param building_id
     * @param callback
     */
    public void updateBuilding(final String building_id, final ICityBuildingRequest.UpdateBuildingCallback callback) {
        String url = ApiConst.BASE_URL + "v1/building";
        String timestamp = String.valueOf(DateUtil.getCurrentTimeInLong());
        String uid = (String) SPUtil.get(context, SPUtil.UID, "");
        String token = (String) SPUtil.get(context, SPUtil.TOKEN, "");
        String sign = parseUpdateBuildingSign(building_id, url, uid, token,timestamp);
        HttpResultObserver respObserver = new HttpResultObserver<UpdateBuildResultEntity>() {

            @Override
            public void onPrepare() {

            }

            @Override
            public void onReconnect() {
                updateBuilding(building_id, callback);
            }

            @Override
            public void onSuccess(UpdateBuildResultEntity data) {
                if (data == null) {
                    callback.onUpdateBuildingFailed("数据出错了");
                    return;
                }
                SPUtil.put(context,SPUtil.BUILDING_ID,data.getBuilding_id());
                SPUtil.put(context,SPUtil.BUILDING_NAME,data.getBuilding_name());
                callback.onUpdateBuildingSuccess();
            }

            @Override
            public void onFail(String msg) {
                LogUtil.d("yongyuan,w", "updateBuilding failed");
                callback.onUpdateBuildingFailed(msg);
            }
        };
        HttpManager.getRetrofit()
                .create(CityBuildingService.class)
                .building(building_id, uid, token, timestamp, sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respObserver);
    }

    /**
     * 生成获取城市列表签名
     *
     * @param url
     * @param uid
     * @param token
     * @return
     */
    private String parseCitySign(String url, String uid, String token,String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    /**
     * 生成获取指定城市的写字楼列表数据签名
     *
     * @param city_id
     * @param url
     * @param uid
     * @param token
     * @return
     */
    private String parseBuildCitySign(String city_id, String url, String uid, String token,String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("city_id", city_id);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.GET, url, paramMap);
        return sign;
    }

    /**
     * 生成设置所在的写字楼签名
     *
     * @param building_id
     * @param url
     * @param uid
     * @param token
     * @return
     */
    private String parseUpdateBuildingSign(String building_id, String url, String uid, String token,String timestamp) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("building_id", building_id);
        paramMap.put("uid", uid);
        paramMap.put("token", token);
        paramMap.put("timestamp", timestamp);
        String sign = HttpUtil.sign(HttpUtil.POST, url, paramMap);
        return sign;
    }


}
