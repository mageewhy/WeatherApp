package kh.edu.rupp.ite.weatherapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Status;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.model.api.service.APIService;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentHomeBinding;
import kh.edu.rupp.ite.weatherapp.viewmodel.WeatherViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private WeatherViewModel viewModel = new WeatherViewModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.LoadWeather();

        viewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<ApiData<Weather>>() {
            @Override
            public void onChanged(ApiData<Weather> weatherApiData) {
                switch (weatherApiData.getStatus()) {
                    case PROCESSING:
                        Toast.makeText(getContext(), "Fetching Data", Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        ShowWeather(weatherApiData.getData());
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), "Received Failed", Toast.LENGTH_LONG).show();
                        break;
                }
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
