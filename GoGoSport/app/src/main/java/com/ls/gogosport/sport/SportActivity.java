package com.ls.gogosport.sport;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.ls.gogosport.R;

public class SportActivity extends AppCompatActivity {

    /**
     * 地图相关
     */
    private MapView sportMapView;
    private BaiduMap baiduMap;

    private static SportActivity instance;

    protected SportHandler handler;
    public static int UPDATE_MAP_LOCATION = 1;
    public static int FIRST_LOCATION = 2;
    public static int DRAW_SPORT_LINE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        init();
    }

    private void init() {
        instance = this;
        handler = new SportHandler();
        sportMapView = findViewById(R.id.sport_map_view);
        baiduMap = sportMapView.getMap();

        baiduMap.setMyLocationEnabled(true);  //开启定位图层
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, SportService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sportMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sportMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, SportService.class));
        sportMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        baiduMap.clear();
        instance = null;
    }

    /**
     * 获取SportActivity实例
     * @return SportActivity实例
     */
    public static SportActivity getInstance() {
        return instance;
    }

    /**
     * 获取SportActivity主线程Handler
     * @return handler
     */
    public SportHandler getHandler() {
        return handler;
    }

    public BaiduMap getBaiduMap() {
        return baiduMap;
    }

    static class SportHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == UPDATE_MAP_LOCATION) {
                MyLocationData locationData = (MyLocationData) msg.obj;
                instance.baiduMap.setMyLocationData(locationData);
                return;
            }

            if (what == FIRST_LOCATION) {
                MapStatus status = (MapStatus) msg.obj;
                instance.baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(status));
                return;
            }

            if (what == DRAW_SPORT_LINE) {
                PolylineOptions options = (PolylineOptions) msg.obj;
                instance.baiduMap.addOverlay(options);
            }
        }
    }
}
