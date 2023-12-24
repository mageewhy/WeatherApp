package kh.edu.rupp.ite.weatherapp.model.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {
    @SerializedName("forecastday")
    private List<Forecastday> forecastday;

    public List<Forecastday> getForecastday() {
        return forecastday;
    }

    public void setForecastday(Forecastday forecastday) {
        this.forecastday.set(0, forecastday);
    }
}
