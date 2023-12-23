package kh.edu.rupp.ite.weatherapp.api.service;

import java.util.ArrayList;

import kh.edu.rupp.ite.weatherapp.api.model.Location;
import kh.edu.rupp.ite.weatherapp.api.model.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("/v1/forecast.json?key=25714936cd1e401ea1270735231610&q=Phnom%20Penh")
    Call<Weather> LoadWeather();

    @GET("/v1/current.json")
    Call<Weather> getCurrentWeather(@Query("key") String apiKey, @Query("q") String query);

}
