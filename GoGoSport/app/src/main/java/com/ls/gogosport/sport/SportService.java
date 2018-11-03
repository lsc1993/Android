package com.ls.gogosport.sport;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.ls.gogosport.SportApplication;

import java.util.ArrayList;

/**
 * 运动服务，提供地图定位,运动轨迹绘制,运动数据计算
 *
 * @author liushuang 2018-07-29
 */
public class SportService extends Service {

    /**
     * 定位相关
     */
    private LocationClient locationClient;
    private boolean isFirstLocation = true; // 是否是第一次定位

    private final static int POINTS_NUM_ONCE_DRAW = 4; // 绘制运动轨迹是每次画多少个点

    private ArrayList<Double> latitudePoints = new ArrayList<>(POINTS_NUM_ONCE_DRAW);
    private ArrayList<Double> longitudePoints = new ArrayList<>(POINTS_NUM_ONCE_DRAW);
    private ArrayList<LatLng> tracePoints = new ArrayList<>(POINTS_NUM_ONCE_DRAW); //记录轨迹的点

    //~ 构造函数
    public SportService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        LocationListener locationListener = new LocationListener();
        locationClient = new LocationClient(SportApplication.getApplication());
        locationClient.registerLocationListener(locationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);  //设置定位模式为高精度
        option.setOpenGps(true); // 打开gps
        option.setLocationNotify(true); //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000); // 定位刷新频率
        option.setIgnoreKillProcess(true); //可选，定位SDK内部是一个service，并放到了独立进程。
        locationClient.setLocOption(option);
        locationClient.start();
        locationClient.requestLocation();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clear();
    }

    private void clear() {
        locationClient.stop();
        latitudePoints.clear();
        longitudePoints.clear();
        tracePoints.clear();
    }

    private class LocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            // 如果是第一次定位,设置地图缩放和定位到当前位置
            if (isFirstLocation) {
                isFirstLocation = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                Message firstLocMsg = SportActivity.getInstance().getHandler().obtainMessage(SportActivity.FIRST_LOCATION);
                firstLocMsg.obj = builder.build();
                SportActivity.getInstance().getHandler().sendMessage(firstLocMsg);
            }

            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(0)
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            SportActivity.getInstance().getBaiduMap().setMyLocationData(locationData);
            latitudePoints.add(location.getLatitude());
            longitudePoints.add(location.getLongitude());

            if (latitudePoints.size() == POINTS_NUM_ONCE_DRAW && longitudePoints.size() == POINTS_NUM_ONCE_DRAW) {
                //绘制运动轨迹
                for (int i = 0; i < POINTS_NUM_ONCE_DRAW; ++i) {
                    tracePoints.add(new LatLng(latitudePoints.get(i), longitudePoints.get(i)));
                }
                SportActivity.getInstance().getBaiduMap().addOverlay(new PolylineOptions().width(8).color(0xAAFF0000).points(tracePoints));
                resetPoint();
            }
        }
    }

    private void resetPoint() {
        double lastLat = latitudePoints.get(POINTS_NUM_ONCE_DRAW - 1);
        double lastLon = longitudePoints.get(POINTS_NUM_ONCE_DRAW - 1);
        LatLng lastPoint = tracePoints.get(POINTS_NUM_ONCE_DRAW - 1);
        latitudePoints.clear();
        longitudePoints.clear();
        tracePoints.clear();
        latitudePoints.add(lastLat);
        longitudePoints.add(lastLon);
        tracePoints.add(lastPoint);
    }
}
