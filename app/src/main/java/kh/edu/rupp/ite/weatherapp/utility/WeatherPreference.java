package kh.edu.rupp.ite.weatherapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class WeatherPreference {
    private static WeatherPreference instance;
    private SharedPreferences sp;
    private WeatherPreference(Context context) {
        sp = context.getSharedPreferences("myWeatherData", Context.MODE_PRIVATE);
    }

    // Public static method to get the singleton instance
    public static WeatherPreference getInstance(Context context) {
        if (instance == null) {
            synchronized (WeatherPreference.class) {
                if (instance == null) {
                    instance = new WeatherPreference(context);
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

    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    public void removeKey(String key) {
        sp.edit().remove(key).apply();
    }
}
