package com.ls.gogosport.main.mainpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ls.gogosport.R;
import com.ls.gogosport.main.MainActivity;

/**
 * 计步页面,展示当日步数、一周内步数统计
 *
 * @author liushuang
 */
public class CountPageFragment extends Fragment implements MainActivity.OnCountStepChanged {

    private TextView countStepValue;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_count_page_fragment, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        countStepValue = root.findViewById(R.id.count_step_value);
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
}
