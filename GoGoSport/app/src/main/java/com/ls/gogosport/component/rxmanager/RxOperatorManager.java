package com.ls.gogosport.component.rxmanager;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * RxAndroid操作管理类
 * 用来管理Rx操作资源的释放
 *
 * @author liushuang 2018-12-01
 */
public class RxOperatorManager {

    private static RxOperatorManager rxManager;

    //用于在服务结束时释放的资源
    private ArrayList<Disposable> commonDispose;

    private RxOperatorManager() {
        commonDispose = new ArrayList<>();
    }

    public static RxOperatorManager getInstance() {
        if (rxManager == null) {
            rxManager = new RxOperatorManager();
        }

        return rxManager;
    }

    /**
     * 添加dispose资源
     *
     * @param disposable 订阅操作
     */
    public void addCommonDispose(Disposable disposable) {
        commonDispose.add(disposable);
    }

    /**
     * 释放所有Rx操作资源
     */
    public void releaseCommonDispose() {
        for (Disposable disposable : commonDispose) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }
}
