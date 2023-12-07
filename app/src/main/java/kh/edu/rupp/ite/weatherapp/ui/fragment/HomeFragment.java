package kh.edu.rupp.ite.weatherapp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.api.service.APIService;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentHomeBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoadWeatherFromServer();
    }

    private void LoadWeatherFromServer() {
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
                    ShowWeather(response.body());
                }else{
                    Toast.makeText(getContext(), "Received Failed" , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Toast.makeText(getContext(), "Received failed" + t, Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void ShowWeather(Weather weather) {
        binding.tempBig.setText(Float.toString(weather.getCurrent().getTemp_c()) + "°C");
        binding.location.setText(weather.getLocation().getName() + ", " + weather.getLocation().getCountry());
        binding.updatedStatus.setText(weather.getCurrent().getLastUpdated());

        binding.tempText.setText(Float.toString(weather.getForecast().getForecastday().getDay().getMaxtemp_c())+ "°C | " + Float.toString(weather.getForecast().getForecastday().getDay().getMaxtemp_c()) + "°C");

        binding.windSpeed.setText(Float.toString(weather.getCurrent().getWind_kph()));
        binding.rainChance.setText(Integer.toString(weather.getForecast().getForecastday().getDay().getDaily_chance_of_rain()) + "% Chance");
    }

}
