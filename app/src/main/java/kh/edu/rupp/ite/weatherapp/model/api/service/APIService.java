package kh.edu.rupp.ite.weatherapp.model.api.service;

import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("/v1/forecast.json")
    Call<Weather> LoadWeather(@Query("key") String apiKey, @Query("q") String cityName);

    @GET("/v1/current.json")
    Call<Weather> LoadCurrentLocationWeather(@Query("key") String apiKey, @Query("q") String query);

}
