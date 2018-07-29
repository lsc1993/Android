package com.ls.gogosport;

import android.app.Application;

public class SportApplication extends Application {

    private static SportApplication application;

    public SportApplication() {}

    public static Application getApplication() {
        if (application == null) {
            application = new SportApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
