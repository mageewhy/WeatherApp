package kh.edu.rupp.ite.weatherapp.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentHomeBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Hour;
import kh.edu.rupp.ite.weatherapp.ui.adapter.HourlyForecastAdapter;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.utility.SettingPreference;
import kh.edu.rupp.ite.weatherapp.viewmodel.WeatherViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private final WeatherViewModel viewModel = new WeatherViewModel();
    private final HourlyForecastAdapter hourlyForecastAdapter = new HourlyForecastAdapter();
    private final LocalDateTime currentTime = LocalDateTime.now();
    private String temp;
    private String speed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.recyclerHourlyForecast.setAdapter(hourlyForecastAdapter);

        viewModel.LoadWeather(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.temp = SettingPreference.getInstance(getContext()).getKeyValue("temp");
        this.speed = SettingPreference.getInstance(getContext()).getKeyValue("speed");

        ConstraintLayout constraintLayout = binding.showMain.findViewById(R.id.show_main);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform actions when the ConstraintLayout is clicked
                viewModel.LoadWeather(getContext());
            }
        });

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
                        ShowHourlyCastList(Arrays.asList(weatherData.getForecast().getForecastday().get(0).getHour()));
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

    private void ShowHourlyCastList(List<Hour> hours) {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerHourlyForecast.setLayoutManager(gridLayoutManager);

        HourlyForecastAdapter adapter = new HourlyForecastAdapter();

        LocalDateTime currentHour = LocalDateTime.now();
        List<Hour> reorderedHours = reorderHoursList(currentHour, hours);

        adapter.submitList(reorderedHours);
        binding.recyclerHourlyForecast.setAdapter(adapter);
    }

    private List<Hour> reorderHoursList(LocalDateTime currentHour, List<Hour> hours) {
        List<Hour> reorderedList = new ArrayList<>();
        int currentHourIndex = -1;

        // Find the index of the current hour in the original list
        for (int i = 0; i < hours.size(); i++) {
            LocalDateTime hourTime = LocalDateTime.parse(hours.get(i).getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            if (hourTime.getHour() == currentHour.getHour()) {
                currentHourIndex = i;
                break;
            }
        }

        if (currentHourIndex != -1) {
            // Add hours from the current hour to the end of the list
            for (int i = currentHourIndex; i < hours.size(); i++) {
                reorderedList.add(hours.get(i));
            }

            // Add hours from the start to the current hour
            for (int i = 0; i < currentHourIndex; i++) {
                reorderedList.add(hours.get(i));
            }
        }

        return reorderedList;
    }



}
