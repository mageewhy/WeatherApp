package kh.edu.rupp.ite.weatherapp.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Status;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.model.api.service.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherViewModel extends ViewModel {

    private MutableLiveData<ApiData<Weather>> _weatherData = new MutableLiveData<>();
    private MutableLiveData<ApiData<List<Weather>>> _weatherLocationData = new MutableLiveData<>();
    private List<Weather> weatherList = new ArrayList<>();
    private String apiKey = "25714936cd1e401ea1270735231610";

    public LiveData<ApiData<List<Weather>>> getWeatherLocationData() {
        return _weatherLocationData;
    }
    public LiveData<ApiData<Weather>> getWeatherData() {
        return _weatherData;
    }
    public void LoadWeather() {
        ApiData<Weather> weatherApiData = new ApiData<>(Status.PROCESSING, null);
        _weatherData.postValue(weatherApiData);

        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = httpClient.create(APIService.class);

        Call<Weather> weather = apiService.LoadWeather();

        weather.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if(response.isSuccessful()){
                    ApiData<Weather> weatherApiData = new ApiData<>(Status.SUCCESS, response.body());
                    _weatherData.postValue(weatherApiData);
                }else{
                    ApiData<Weather> weatherApiData = new ApiData<>(Status.ERROR, null);
                    _weatherData.postValue(weatherApiData);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                ApiData<Weather> weatherApiData = new ApiData<>(Status.ERROR, null);
                _weatherData.postValue(weatherApiData);
            }
        });
    }


    public void LoadLocationData(String cityName) {
        ApiData<List<Weather>> weatherLocationData = new ApiData<List<Weather>>(Status.PROCESSING, null) {
        };
        _weatherLocationData.postValue(weatherLocationData);

        // Retrofit initialization and API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService weatherApi = retrofit.create(APIService.class);
        Call<Weather> call = weatherApi.LoadCurrentLocationWeather(apiKey, cityName);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    Weather weather = response.body();
                    weatherList.add(weather);

                    ApiData<List<Weather>> locationAPIData = new ApiData<>(Status.SUCCESS, weatherList);
                    _weatherLocationData.postValue(locationAPIData);
                } else {
                    // Handle the unsuccessful response
                    ApiData<List<Weather>> locationAPIData = new ApiData<>(Status.ERROR, null);
                    _weatherLocationData.postValue(locationAPIData);
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                // Handle the network failure
                ApiData<List<Weather>> locationAPIData = new ApiData<>(Status.ERROR, null);
                _weatherLocationData.postValue(locationAPIData);
                Log.d("WeatherViewModel", "API call failed: " + t.getMessage());
            }
        });
    }
    public void refreshLocationData(String cityName) {
        // Clear the existing weather list
        weatherList.clear();

        // Call the API to load the location data again
        LoadLocationData(cityName);
    }
}
