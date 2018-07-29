package com.ls.gogosport.main;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ls.gogosport.R;
import com.ls.gogosport.main.mainpage.MainPageFragment;
import com.ls.gogosport.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * 初始化页面
     */
    private void init() {
        StatusBarUtil.setStatusBarColor(this, R.color.title_bar_bg);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main_page_fragment_container, MainPageFragment.newInstance());
        ft.commit();
    }
}
