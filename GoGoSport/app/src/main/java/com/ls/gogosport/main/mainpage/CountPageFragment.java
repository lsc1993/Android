package com.ls.gogosport.main.mainpage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ls.gogosport.R;
import com.ls.gogosport.component.rxmanager.RxCommonOperator;
import com.ls.gogosport.component.view.LineChartView;
import com.ls.gogosport.main.MainActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 计步页面,展示当日步数、一周内步数统计
 *
 * @author liushuang
 */
public class CountPageFragment extends Fragment implements MainActivity.OnCountStepChanged, RxCommonOperator.OnTimeChangeListener {

    private TextView countStepValue;
    private TextView countTime;
    private LineChartView lineView;

    private View root;

    public CountPageFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CountPageFragment.
     */
    public static CountPageFragment newInstance() {
        return new CountPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册计时器接口
        RxCommonOperator.getInstance().setTimeChangeListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.main_count_page_fragment, container, false);
            initView(root);
        }
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        return root;
    }

    private void initView(View root) {
        countStepValue = root.findViewById(R.id.count_step_value);
        countTime = root.findViewById(R.id.count_time_value);

        lineView = root.findViewById(R.id.step_line_chart_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initDate();
        initHistoryStep();
        lineView.invalidate();
    }

    /**
     * 初始化历史步数
     */
    private void initHistoryStep() {
        int[] stepData = new int[7];
        float maxStep = 0f;
        Random random = new Random(1);
        for (int i = 0; i < 7; ++i) {
            stepData[i] = random.nextInt(10000);
            if (maxStep < stepData[i]) {
                maxStep = stepData[i];
            }
        }

        float[] heightRate = new float[stepData.length];
        for (int i = 0;i < stepData.length; i++) {
            heightRate[i] = stepData[i] / maxStep * 0.95f;
        }

        lineView.setHeightRate(heightRate);
        lineView.setStepData(stepData);
    }

    /**
     * 初始化折线图日期,显示一周
     */
    private void initDate() {
        //初始化日期,这里显示一周的数据
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String[] weekCurr = new String[7];
        weekCurr[6] = "今天";
        for (int i = 0; i < 6; ++i) {
            weekCurr[i] = weeks[(currIndex + i + 1) % 7];
        }

        lineView.setWeekDay(weekCurr);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxCommonOperator.getInstance().setTimeChangeListener(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void updateStep(int step) {
        countStepValue.setText(String.valueOf(step));
    }

    @Override
    public void onTimeChange(long time) {
        String second = String.format(Locale.getDefault(), "%02d", time % 60);
        String minute = String.format(Locale.getDefault(), "%02d", time / 60);
        String hour = String.format(Locale.getDefault(), "%02d", time / (60 * 60));
        String timeNow = String.format("%s:%s:%s", hour, minute, second);
        countTime.setText(String.valueOf(timeNow));
    }
}
