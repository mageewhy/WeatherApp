package kh.edu.rupp.ite.weatherapp.api.service;

import android.content.Context;
import android.widget.Toast;

import kh.edu.rupp.ite.weatherapp.api.model.Weather;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationRepo {
    private APIService apiService;


    public LocationRepo() {
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = httpClient.create(APIService.class);

    }

    public void loadWeather(final LoadWeatherCallback callback, String apiKey, String query) {
        Call<Weather> weatherCall = apiService.LoadLocationWeather(apiKey, query);
        weatherCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    callback.onWeatherLoaded(response.body());
                } else {
                    callback.onWeatherLoadFailed();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                callback.onWeatherLoadFailed();
            }
        });
    }

    public interface LoadWeatherCallback {
        void onWeatherLoaded(Weather weather);

        void onWeatherLoadFailed();
    }
}