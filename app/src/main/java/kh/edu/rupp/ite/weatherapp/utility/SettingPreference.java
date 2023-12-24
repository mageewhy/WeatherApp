package kh.edu.rupp.ite.weatherapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreference {
    private static SettingPreference instance;
    private final SharedPreferences sp;
    private SettingPreference(Context context) {
        sp = context.getSharedPreferences("mySetting", Context.MODE_PRIVATE);
    }

    // Public static method to get the singleton instance
    public static SettingPreference getInstance(Context context) {
        if (instance == null) {
            synchronized (SettingPreference.class) {
                if (instance == null) {
                    instance = new SettingPreference(context);
                }
            }
        }
        return instance;
    }

    public void storeKey(String key, String data) {
        sp.edit().putString(key, data).apply();
    }

    public String getKeyValue(String key) {
        return sp.getString(key, "");
    }
}
