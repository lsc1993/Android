package com.ls.gogosport.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ls.gogosport.main.MainActivity;
import com.ls.gogosport.R;
import com.ls.gogosport.util.LogUtil;
import com.ls.gogosport.util.StatusBarUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Splash 欢迎页
 * 用于初始化资源以及现实欢迎页面
 *
 * @author liushuang 2018-06-36
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setStatusBarTranslucent(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMainActivity();
    }

    /**
     * 启动MainActivity,延迟1500mills
     */
    private void startMainActivity() {
        LogUtil.d(TAG, "startMainActivity");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
