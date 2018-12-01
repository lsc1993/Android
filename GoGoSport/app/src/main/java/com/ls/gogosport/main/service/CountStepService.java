package com.ls.gogosport.main.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import com.ls.gogosport.count.CountStepCalculator;

/**
 * 计步服务
 */
public class CountStepService extends Service {

    private IBinder countStepBinder = new CountStepBinder();

    private SensorManager sensorManager;
    private CountStepCalculator stepCalculator;

    public CountStepService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void init() {
        stepCalculator = new CountStepCalculator();

        //注册加速度计
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(stepCalculator, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            }
        }
    }

    public int getStep() {
        return stepCalculator.getStep();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return countStepBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(stepCalculator);
    }

    public class CountStepBinder extends Binder {
        public CountStepService getService() {
            return CountStepService.this;
        }
    }
}