package com.ls.gogosport.main.mainpage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.ls.gogosport.R;

/**
 * 设置页面,提供设置功能
 *
 * @author liushuang 2018-07-11
 */
public class SettingFragment extends PreferenceFragmentCompat implements PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    public SettingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting);
    }

    public Fragment getCallbackFragment() {
        return this;
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
    public boolean onPreferenceStartScreen(PreferenceFragmentCompat caller, PreferenceScreen pref) {
        //caller.onNavigateToScreen(pref);
        caller.setPreferenceScreen(pref);
        caller.scrollToPreference(pref);
        //getPreferenceScreen().findPreference("setting_speak_type").setDependency("setting_speak_switch");
        //caller.getPreferenceManager().showDialog(pref);
        return true;
    }
}
