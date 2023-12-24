package kh.edu.rupp.ite.weatherapp.model.api.model;

import com.google.gson.annotations.SerializedName;

public class Current {
    @SerializedName("last_updated")
    private String lastUpdated;
    @SerializedName("temp_c")
    private float temp_c;
    @SerializedName("temp_f")
    private float temp_f;
    @SerializedName("wind_mph")
    private float wind_mph;
    @SerializedName("wind_kph")
    private float wind_kph;
    @SerializedName("cloud")
    private int cloud;

    private Condition condition;

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public float getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(float temp_c) {
        this.temp_c = temp_c;
    }

    public float getTemp_f() {
        return temp_f;
    }

    public void setTemp_f(float temp_f) {
        this.temp_f = temp_f;
    }

    public float getWind_mph() {
        return wind_mph;
    }

    public void setWind_mph(float wind_mph) {
        this.wind_mph = wind_mph;
    }

    public float getWind_kph() {
        return wind_kph;
    }

    public void setWind_kph(float wind_kph) {
        this.wind_kph = wind_kph;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
