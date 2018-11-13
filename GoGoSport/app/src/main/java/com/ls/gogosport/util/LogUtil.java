package com.ls.gogosport.util;

import android.util.Log;

public class LogUtil {

    private static boolean LOG_ON = true;

    public static void d(String tag, String msg) {
        if (LOG_ON) {
            Log.d(tag, msg);
        }
    }
}
