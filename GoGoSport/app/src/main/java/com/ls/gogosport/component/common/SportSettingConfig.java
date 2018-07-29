package com.ls.gogosport.component.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ls.gogosport.SportApplication;

/**
 * 获取运动设置:语音播报、播报频率、播报类型
 *
 * @author liushuang 2018-07-29
 */
public class SportSettingConfig {

    private static SportSettingConfig instance;
    private SharedPreferences sportConfig;

    private SportSettingConfig() {
        sportConfig = PreferenceManager.getDefaultSharedPreferences(SportApplication.getApplication());
    }

    public static SportSettingConfig getInstance() {
        if (instance == null) {
            instance = new SportSettingConfig();
        }

        return instance;
    }

    /**
     * 是否开启了语音播报功能
     * @return true 开启语音播报 false 不开启
     */
    public boolean getSpeakEnable() {
        return sportConfig.getBoolean("setting_speak_switch", false);
    }

    /**
     * 获取语音播报频率
     * @return 播报频率
     */
    public int getSpeakRate() {
        return sportConfig.getInt("setting_speak_rate", 15);
    }

    /**
     * 获取语音播报类型
     * @return 播报类型
     */
    public String getSpeakType() {
        return sportConfig.getString("setting_speak_type", "");
    }
}
