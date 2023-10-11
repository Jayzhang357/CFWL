package com.wl.comom;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;

public class MyLocationListener extends BDAbstractLocationListener {
    private BaiduMap baiduMap;

    public MyLocationListener(BaiduMap baiduMap) {
        this.baiduMap = baiduMap;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // 获取到当前定位的经纬度
        double latitude = bdLocation.getLatitude();
        double longitude = bdLocation.getLongitude();
        LatLng currentLatLng = new LatLng(latitude, longitude);

        // 将地图调整到当前位置
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(currentLatLng));
    }
}
