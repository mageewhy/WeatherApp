package kh.edu.rupp.ite.weatherapp.api.service;


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

}