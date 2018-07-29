package com.ls.gogosport.main.mainpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ls.gogosport.R;
import com.ls.gogosport.sport.SportActivity;

/**
 * GoGoSport主页片段,构建主页页面
 * @author liushuang 2018-07-04
 */
public class MainPageFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fm;

    public MainPageFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.page_main_fragment_main, container, false);
        initView(root);
        return root;
    }

    private void init() {
        fm = getActivity().getSupportFragmentManager();
    }

    private void initView(View root) {
        ImageView historyImg = root.findViewById(R.id.history_image_btn);
        historyImg.setOnClickListener(this);
        ImageView settingImg = root.findViewById(R.id.settting_image_btn);
        settingImg.setOnClickListener(this);

        Button startRun = root.findViewById(R.id.start_run_btn);
        startRun.setOnClickListener(this);

        initTitle(root);
    }

    private void initTitle(View root) {
        Toolbar toolbar = root.findViewById(R.id.title_container);
        ((TextView)toolbar.findViewById(R.id.title_text)).setText(R.string.sport_title);
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.history_image_btn) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_page_fragment_container, new HistoryFragment());
            ft.addToBackStack(null);
            ft.commit();
            return;
        }

        if (id == R.id.settting_image_btn) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_page_fragment_container, new SettingFragment());
            ft.addToBackStack(null);
            ft.commit();
            return;
        }

        if (id == R.id.start_run_btn) {
            Intent intent = new Intent(getActivity(), SportActivity.class);
            startActivity(intent);
            return;
        }
    }
}
