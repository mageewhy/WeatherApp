package kh.edu.rupp.ite.weatherapp.viewmodel;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
}
