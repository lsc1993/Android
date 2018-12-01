package com.ls.gogosport.count;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.ls.gogosport.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 计步类,提供计步实现
 * 通过使用加速度传感器实时的统计步数变化
 *
 * @author liushuang
 * @see {Sensor.TYPE_ACCELEROMETER}
 */
public class CountStepCalculator implements SensorEventListener {
    private static final String TAG = "CountStepCalculator";

    private int MAX_THRESHOLD = 90;  //波峰波谷阈值
    private int MIN_THRESHOLD = 15;  //波峰波谷阈值
    private int MAX_STEP_DISTANCE = 20;  //最大步长
    private int MIN_STEP_DISTANCE = 5;  //最短步长
    private int WAVE_BUFFER_NUMBER = 40; //数据的缓存长度
    private int WAVE_FILTER_NUMBER = 8;  //滤波的缓存数据长度
    private int stepWindow = 0;

    private ArrayList<Double> filterWaveData;
    private ArrayList<Double> bufferWaveData;

    private int step = 0;

    public CountStepCalculator() {
        filterWaveData = new ArrayList<>(WAVE_FILTER_NUMBER);
        bufferWaveData = new ArrayList<>(WAVE_BUFFER_NUMBER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //计算平均加速度
        double accelerate = Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2)) * 10;
        //LogUtil.d(TAG, "accelerate=" + accelerate + " value[0]=" + event.values[0] + " value[1]=" + event.values[1] + " value[2]=" + event.values[2]);
        double acc = filterWave(accelerate);
        if (acc > 0) {
            countStep(acc);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //根据传感器灵敏度改变阈值
        if (accuracy == SensorManager.SENSOR_DELAY_FASTEST) {
            MAX_THRESHOLD = 100;
            MIN_THRESHOLD = 15;
            MAX_STEP_DISTANCE = 25;
            MIN_STEP_DISTANCE = 5;
        } else if (accuracy == SensorManager.SENSOR_DELAY_NORMAL) {
            MAX_THRESHOLD = 80;
            MIN_THRESHOLD = 10;
            MAX_STEP_DISTANCE = 30;
            MIN_STEP_DISTANCE = 10;
        }
    }

    private void countStep(double accelerate) {
        LogUtil.d(TAG, "bufferWaveData.size() = " + bufferWaveData.size() + " acc= " + accelerate);
        if (bufferWaveData.size() < WAVE_BUFFER_NUMBER) {
            bufferWaveData.add(accelerate);
        } else {
            stepWindow++; //计步窗口加1
            ArrayList<Double> gradient = new ArrayList<>(); //斜率
            ArrayList<Point> peakPos = new ArrayList<>(); //波峰位置
            ArrayList<Point> valleyPos = new ArrayList<>(); //波谷位置

            for (int i = 1; i < WAVE_BUFFER_NUMBER; ++i) {
                //斜率计算,根据斜率确定波峰.由于x轴递增,所以这里只计算y轴的差值来确定斜率的正负
                LogUtil.d(TAG, "gradient= " + bufferWaveData.get(i) + " - " + bufferWaveData.get(i-1) + " = " + (bufferWaveData.get(i) - bufferWaveData.get(i - 1)));
                gradient.add(bufferWaveData.get(i) - bufferWaveData.get(i - 1));
            }

            for (int i = 1; i < gradient.size(); ++i) {

                //斜率由正变为负,判定为波峰
                if (gradient.get(i) <= 0 && gradient.get(i - 1) > 0) {
                    peakPos.add(new Point(stepWindow, i - 1, bufferWaveData.get(i - 1)));
                }
                //斜率由负变为正,判定为波谷
                if (gradient.get(i) >= 0 && gradient.get(i - 1) < 0) {
                    valleyPos.add(new Point(stepWindow, i - 1, bufferWaveData.get(i - 1)));
                }
            }

            LogUtil.d(TAG, "peak size " + peakPos.size() + " valley size " + valleyPos.size());
            //波峰和波谷的统计数据都有值
            if (peakPos.size() > 0 && valleyPos.size() > 0) {
                if (peakPos.size() >= valleyPos.size()) {
                    Iterator<Point> peakIterator = peakPos.iterator();
                    Iterator<Point> valleyIterator = valleyPos.iterator();
                    while (valleyIterator.hasNext()) {
                        Point peakPoint = peakIterator.next();
                        Point valleyPoint = valleyIterator.next();
                        //获取x轴坐标
                        int peakX = peakPoint.xPos + WAVE_BUFFER_NUMBER * (peakPoint.stepWindow - 1);
                        int valleyX = valleyPoint.xPos + WAVE_BUFFER_NUMBER * (valleyPoint.stepWindow - 1);
                        //获取y轴坐标
                        double peakY = peakPoint.yPos;
                        double valleyY = valleyPoint.yPos;
                        LogUtil.d(TAG, "1 threshold=" + (peakY - valleyY) + " step distance=" + (peakX - valleyX));
                        //符合阈值范围,步数加1
                        if (Math.abs(peakY - valleyY) >= MIN_THRESHOLD && Math.abs(peakY - valleyY) <= MAX_THRESHOLD
                                && Math.abs(peakX - valleyX) <= MAX_STEP_DISTANCE && Math.abs(peakX - valleyX) >= MIN_STEP_DISTANCE) {
                            step++;
                            LogUtil.d(TAG, "Step = " + step);
                        }

                        peakIterator.remove();
                        valleyIterator.remove();
                    }
                } else {
                    Iterator<Point> peakIterator = peakPos.iterator();
                    Iterator<Point> valleyIterator = valleyPos.iterator();
                    while (peakIterator.hasNext()) {
                        Point peakPoint = peakIterator.next();
                        Point valleyPoint = valleyIterator.next();
                        //获取x轴坐标
                        int peakX = peakPoint.xPos + WAVE_BUFFER_NUMBER * (peakPoint.stepWindow - 1);
                        int valleyX = valleyPoint.xPos + WAVE_BUFFER_NUMBER * (valleyPoint.stepWindow - 1);
                        //获取y轴坐标
                        double peakY = peakPoint.yPos;
                        double valleyY = valleyPoint.yPos;
                        LogUtil.d(TAG, "2 threshold=" + Math.abs(peakY - valleyY) + "step distance=" + (peakX - valleyX));
                        //符合阈值范围,步数加1
                        if (Math.abs(peakY - valleyY) >= MIN_THRESHOLD && Math.abs(peakY - valleyY) <= MAX_THRESHOLD
                                && Math.abs(peakX - valleyX) <= MAX_STEP_DISTANCE && Math.abs(peakX - valleyX) >= MIN_STEP_DISTANCE) {
                            step++;
                            LogUtil.d(TAG, "Step = " + step);
                        }

                        peakIterator.remove();
                        valleyIterator.remove();
                    }
                }
            }
            bufferWaveData.clear();
        }
    }

    /**
     * 滤波算法,使波形更加平滑
     * 采用计算加速度平均数的方式滤波
     *
     * @param accelerate 加速度
     * @return 过滤后的加速度
     */
    private double filterWave(double accelerate) {
        filterWaveData.add(accelerate);
        if (filterWaveData.size() == WAVE_FILTER_NUMBER) {
            double sum = 0d;
            for (double acc : filterWaveData) {
                sum += acc;
            }
            filterWaveData.remove(0);
            LogUtil.d(TAG, "sum = " + (sum / WAVE_FILTER_NUMBER));
            return sum / WAVE_FILTER_NUMBER;
        }
        return 0;
    }

    /**
     * 获取当前步数
     *
     * @return 步数
     */
    public int getStep() {
        return step;
    }

    /**
     * 波峰、波谷的数据结构
     * 用于记录波峰波谷的位置信息
     */
    private class Point {

        Point(int stepWindow, int x, double y) {
            this.stepWindow = stepWindow;
            this.xPos = x;
            this.yPos = y;
        }

        int stepWindow; //计步窗口
        int xPos; //x轴的坐标 xPos = x * stepWindow
        double yPos; //y轴坐标
    }
}
