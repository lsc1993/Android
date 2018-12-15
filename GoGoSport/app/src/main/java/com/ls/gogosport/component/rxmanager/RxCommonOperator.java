package com.ls.gogosport.component.rxmanager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * RxAndroid公共操作
 */
public class RxCommonOperator {

    private static RxCommonOperator rxOperator;

    private Observable<Long> timeCounter;
    private OnTimeChangeListener timeChangeListener;

    private RxCommonOperator() {

    }

    public static RxCommonOperator getInstance() {
        if (rxOperator == null) {
            rxOperator = new RxCommonOperator();
        }

        return rxOperator;
    }

    /**
     * 注册计时器
     * 这是一个全局计时器,记录整个计步的时长
     */
    public void registerTimeCounter() {
        if (timeCounter == null) {
            timeCounter = Observable.interval(1, TimeUnit.SECONDS);
            RxOperatorManager.getInstance().addCommonDispose(
                    timeCounter.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                if (timeChangeListener != null) {
                                    timeChangeListener.onTimeChange(aLong);
                                }
                            })
            );
        }
    }

    public void setTimeChangeListener(OnTimeChangeListener timeChangeListener) {
        this.timeChangeListener = timeChangeListener;
    }

    /**
     * 计时器时间改变的回调接口
     */
    public interface OnTimeChangeListener {
        void onTimeChange(long time);
    }
}
