package com.ls.gogosport;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class SportApplication extends Application {

    private static SportApplication application;

    public SportApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化百度地图,设置坐标类型
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    /**
     * 获取Application实例
     *
     * @return Application实例
     */
    public static Application getApplication() {
        return application;
    }
}
