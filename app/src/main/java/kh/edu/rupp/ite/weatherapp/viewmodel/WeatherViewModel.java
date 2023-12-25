package kh.edu.rupp.ite.weatherapp.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import kh.edu.rupp.ite.weatherapp.BuildConfig;
import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Location;
import kh.edu.rupp.ite.weatherapp.model.api.model.Status;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.model.api.service.APIService;
import kh.edu.rupp.ite.weatherapp.utility.WeatherPreference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherViewModel extends ViewModel {

    private final MutableLiveData<ApiData<Weather>> _weatherData = new MutableLiveData<>();
    private final MutableLiveData<ApiData<List<Weather>>> _weatherLocationData = new MutableLiveData<>();
    private final List<Weather> weatherList = new ArrayList<>(); // For API responses
    private List<Weather> weatherListFromPrefs = new ArrayList<>(); // For SharedPreferences data
    private AtomicInteger counter;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private final String apiKey = "25714936cd1e401ea1270735231610";

    public LiveData<ApiData<List<Weather>>> getWeatherLocationData() {
        return _weatherLocationData;
    }

    public LiveData<ApiData<Weather>> getWeatherData() {
        return _weatherData;
    }

    public void LoadWeather(Context context) {
        ApiData<Weather> weatherApiData = new ApiData<>(Status.PROCESSING, null);
        _weatherData.postValue(weatherApiData);

        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = httpClient.create(APIService.class);

        String citName = getCurrentLocationCityName(context);

        Call<Weather> weather = apiService.LoadWeather(apiKey, citName);

        weather.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    ApiData<Weather> weatherApiData = new ApiData<>(Status.SUCCESS, response.body());
                    _weatherData.postValue(weatherApiData);
                } else {
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

    public void LoadLocationData(Context context, String cityName) {
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

                        handleRefreshCompletion(context, weatherList);
                    }
                    else {
                        ApiData<List<Weather>> weatherLocationData = new ApiData<List<Weather>>(Status.ERROR, null) {
                        };
                        _weatherLocationData.postValue(weatherLocationData);
//                        Log.d("WeatherViewModel", "API call failed: ");
                    }

                    refreshLocationData(context);
                }

                @Override
                public void onFailure(Call<Weather> call, Throwable t) {
                    ApiData<List<Weather>> weatherLocationData = new ApiData<List<Weather>>(Status.ERROR, null) {
                    };
                    _weatherLocationData.postValue(weatherLocationData);
//                    Log.d("WeatherViewModel", "API call failed: " + t.getMessage());
                }
            });
        }

    // Method to refresh location data
    private void RefreshLocationData(Context context) {
        List<Weather> updatedWeatherList = new ArrayList<>();

        // Retrofit initialization and API call for each city
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService weatherApi = retrofit.create(APIService.class);


        weatherListFromPrefs = getAllWeatherDataFromSharedPreferences(context);

        for (Weather weather : weatherListFromPrefs) {
            Call<Weather> call = weatherApi.LoadCurrentLocationWeather(apiKey, weather.getLocation().getName());

            counter = new AtomicInteger(0);
            call.enqueue(new Callback<Weather>() {
                @Override
                public void onResponse(Call<Weather> call, Response<Weather> response) {

                    counter.incrementAndGet();
                    if (response.isSuccessful()) {
                        Weather updatedWeather = response.body();
                        updatedWeatherList.add(updatedWeather);
//                        Log.d("RefreshedData", "Refreshing data: " + updatedWeatherList);

                    }

                    if (counter.get() == weatherListFromPrefs.size()) {
                        handleRefreshCompletion(context, updatedWeatherList);
                    }
                }

                @Override
                public void onFailure(Call<Weather> call, Throwable t) {
                    counter.incrementAndGet();
                    if (counter.get() == weatherListFromPrefs.size()) {
                        handleRefreshCompletion(context, updatedWeatherList);
                    }
                    Log.d("WeatherViewModel", "API call failed: " + t.getMessage());
                }
            });
        }
    }

    // Method to handle completion of data refresh
    private void handleRefreshCompletion(Context context, List<Weather> updatedWeatherList) {
        // Notify the LiveData with the updated data
        ApiData<List<Weather>> locationAPIData = new ApiData<>(Status.SUCCESS, updatedWeatherList);
        _weatherLocationData.postValue(locationAPIData);

        // Save the updated weather data into SharedPreferences here
        saveWeatherDataToSharedPreferences(context, updatedWeatherList);
    }

    // Saving Weather data using Gson for serialization
    public void saveWeatherDataToSharedPreferences(Context context, List<Weather> weatherList) {
        Gson gson = new Gson();

        for (Weather weather : weatherList) {
            Location location = weather.getLocation();
            String cityName = location.getName();
            String weatherData = gson.toJson(weather);

            WeatherPreference.getInstance(context).storeKey(cityName, weatherData);
        }
//        Log.d("SaveData", "Saving data:" + WeatherPreference.getInstance(context).getAll());
    }

    public void removeWeatherDataFromSharedPreferences(Context context, String cityName){
        WeatherPreference.getInstance(context).removeKey(cityName);
    }

    // Retrieving Weather data using Gson for deserialization
    public List<Weather> getAllWeatherDataFromSharedPreferences(Context context) {
        Gson gson = new Gson();
        Map<String, ?> allEntries = WeatherPreference.getInstance(context).getAll();

        weatherListFromPrefs.clear();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String weatherJson = entry.getValue().toString();
            Weather weather = gson.fromJson(weatherJson, Weather.class);
            weatherListFromPrefs.add(weather);
        }
//        Log.d("GetAllList", "Get All Data:" + weatherListFromPrefs);
        return weatherListFromPrefs;
    }

    public void refreshLocationData(Context context) {
        RefreshLocationData(context);
    }

    private String getCurrentLocationCityName(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String defaultCityName = "Phnom Penh";
        String cityName = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            cityName = defaultCityName;
            return cityName;
        }


        android.location.Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

        try {
            List<android.location.Address> address = geoCoder.getFromLocation(latitude, longitude, 1);

            cityName = address.get(0).getLocality();

        } catch (IOException e) {}
        catch (NullPointerException e) {}

        return cityName;
    }

}
