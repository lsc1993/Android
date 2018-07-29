package com.ls.gogosport.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ls.gogosport.R;

/**
 * 设置沉浸式状态栏颜色
 *
 * @author liushuang 2018-07-23
 */
public class StatusBarUtil {

    /**
     * 设置当前Activity状态栏颜色
     * @param activity 当前Activity
     * @param color 颜色
     */
    public static void setStatusBarColor(Activity activity, int color) {
        int apiVersion = Build.VERSION.SDK_INT;

        if (apiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            //Android 5.0以上
            getWindow(activity).addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow(activity).clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow(activity).setStatusBarColor(activity.getResources().getColor(R.color.title_bar_bg));
        } else if (apiVersion >= Build.VERSION_CODES.KITKAT) {
            //Android 4.4 到 Android 5.0
            getWindow(activity).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && decorView.getChildAt(count - 1) instanceof View) {
                decorView.getChildAt(count - 1).setBackgroundColor(color);
            } else {
                View statusView = new View(activity);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity));
                statusView.setLayoutParams(params);
                statusView.setBackgroundColor(activity.getResources().getColor(R.color.title_bar_bg));
                decorView.addView(statusView);
            }
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 设置状态栏为透明
     * @param activity 当前Activity
     */
    public static void setStatusBarTranslucent(Activity activity) {
        int apiVersion = Build.VERSION.SDK_INT;
        if (apiVersion >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取窗口实例
     * @param activity 当前Activity
     * @return 窗口实例
     */
    private static Window getWindow(Activity activity) {
        return activity.getWindow();
    }
}
