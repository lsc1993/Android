package com.ls.gogosport.main;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ls.gogosport.R;
import com.ls.gogosport.main.mainpage.CountPageFragment;
import com.ls.gogosport.main.mainpage.SportPageFragment;
import com.ls.gogosport.main.mainpage.UserPageFragment;
import com.ls.gogosport.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout countTab;
    private RelativeLayout sportTab;
    private RelativeLayout userTab;

    private ImageView countImg;
    private TextView countText;

    private ImageView sportImg;
    private TextView sportText;

    private ImageView userImg;
    private TextView userText;

    //FragmentManager
    private FragmentManager fragmentManager;

    private TextView titleText;

    private String countPageTitle;
    private String sportPageTitle;
    private String userPageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        initPage();
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        initTitleBar();
        countPageTitle = getResources().getString(R.string.count_step_page_title);
        setTitleBar(countPageTitle);

        sportPageTitle = getResources().getString(R.string.sport_page_title);
        userPageTitle = getResources().getString(R.string.user_center_page_title);

        countTab = findViewById(R.id.main_page_count_step_container);
        countTab.setOnClickListener(this);
        countImg = findViewById(R.id.main_page_count_step_icon);
        countText = findViewById(R.id.main_page_count_step_text);

        sportTab = findViewById(R.id.main_page_sport_container);
        sportTab.setOnClickListener(this);
        sportImg = findViewById(R.id.main_page_sport_icon);
        sportText = findViewById(R.id.main_page_sport_text);

        userTab = findViewById(R.id.main_page_user_container);
        userTab.setOnClickListener(this);
        userImg = findViewById(R.id.main_page_user_icon);
        userText = findViewById(R.id.main_page_user_text);

        //初始化选中状态
        setTabStatus(true, false, false);
    }

    private void initTitleBar() {
        Toolbar titleBar = findViewById(R.id.title_container);
        titleText = titleBar.findViewById(R.id.title_text);
    }

    private void setTitleBar(String title) {
        titleText.setText(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化页面,显示计步页面
     */
    private void initPage() {
        StatusBarUtil.setStatusBarColor(this, R.color.title_bar_bg);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.page_fragment_container, CountPageFragment.newInstance());
        ft.commit();
    }

    /**
     * 替换页面
     *
     * @param page 页面实例
     */
    private void changePage(@NonNull Fragment page) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.page_fragment_container, page);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        if (v == countTab) {
            setTitleBar(countPageTitle);
            setTabStatus(true, false, false);
            //启动计步页面
            changePage(CountPageFragment.newInstance());
            return;
        }

        if (v == sportTab) {
            setTitleBar(sportPageTitle);
            setTabStatus(false, true, false);
            //启动运动页面
            changePage(SportPageFragment.newInstance());
            return;
        }

        if (v == userTab) {
            setTitleBar(userPageTitle);
            //启动个人中心页面
            changePage(UserPageFragment.newInstance());
            setTabStatus(false, false, true);
        }
    }

    /**
     * 设置tab状态,true选中,false补选中
     *
     * @param status1 第一个tab状态
     * @param status2 第二个tab状态
     * @param status3 第三个tab状态
     */
    private void setTabStatus(boolean status1, boolean status2, boolean status3) {
        countImg.setSelected(status1);
        countText.setSelected(status1);

        sportImg.setSelected(status2);
        sportText.setSelected(status2);

        userImg.setSelected(status3);
        userText.setSelected(status3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
