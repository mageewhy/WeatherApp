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

import com.squareup.picasso.Picasso;

import java.util.List;

import kh.edu.rupp.ite.weatherapp.databinding.FragmentHomeBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Forecastday;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.utility.SettingPreference;
import kh.edu.rupp.ite.weatherapp.viewmodel.WeatherViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private final WeatherViewModel viewModel = new WeatherViewModel();

    private String temp;
    private String speed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.temp = SettingPreference.getInstance(getContext()).getKeyValue("temp");
        this.speed = SettingPreference.getInstance(getContext()).getKeyValue("speed");

        viewModel.LoadWeather();

        viewModel.getWeatherData().observe(getViewLifecycleOwner(), new Observer<ApiData<Weather>>() {
            @Override
            public void onChanged(ApiData<Weather> weatherApiData) {
                switch (weatherApiData.getStatus()) {
                    case PROCESSING:
                        binding.progressBar2.setVisibility(View.VISIBLE);
//                        Toast.makeText(getContext(), "Fetching Data", Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        binding.progressBar2.setVisibility(View.GONE);
                        Weather weatherData = weatherApiData.getData();
                        ShowWeather(weatherData);
                        ShowHourlyCastList(weatherData.getForecast().getForecastday());
                        break;
                    case ERROR:
                        binding.progressBar2.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Network Failed", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }



    @SuppressLint("SetTextI18n")
    private void ShowWeather(Weather weather) {
        Picasso.get().load(weather.getCurrent().getCondition().getIcon()).into(binding.dynamicIcon);
        if (this.temp.equals("°C")) {
            binding.tempBig.setText(weather.getCurrent().getTemp_c() + "°C");
        }
        else {
            binding.tempBig.setText(weather.getCurrent().getTemp_f() + "°F");
        }
        binding.location.setText(weather.getLocation().getName() + ", " + weather.getLocation().getCountry());
        binding.updatedStatus.setText(weather.getCurrent().getLastUpdated());
        binding.rainChance.setText(weather.getForecast().getForecastday().get(0).getDay().getDaily_chance_of_rain() + "% Chance");

        if (this.speed.equals("Km/h")) {
            binding.windSpeed.setText(weather.getCurrent().getWind_kph() + " Km/h");
        } else {
            binding.windSpeed.setText(weather.getCurrent().getWind_mph() + " M/h");
        }

        if (this.temp.equals("°C")) {
            binding.tempText.setText(weather.getForecast().getForecastday().get(0).getDay().getMaxtemp_c() + "°C | " + weather.getForecast().getForecastday().get(0).getDay().getMaxtemp_c() + "°C");
        } else {
            binding.tempText.setText(weather.getForecast().getForecastday().get(0).getDay().getAvgtemp_f() + "°F | " + weather.getForecast().getForecastday().get(0).getDay().getMaxtemp_f() + "°F");
        }
    }

    private void ShowHourlyCastList(List<Forecastday> forecastday) {

    }

}
