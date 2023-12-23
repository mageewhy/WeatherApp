package kh.edu.rupp.ite.weatherapp.ui.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.api.model.Current;
import kh.edu.rupp.ite.weatherapp.api.model.Location;
import kh.edu.rupp.ite.weatherapp.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.api.service.APIService;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentLocationBinding;
import kh.edu.rupp.ite.weatherapp.ui.adapter.LocationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;

    String apiKey = "25714936cd1e401ea1270735231610";
    private LocationAdapter locationAdapter;
    private List<Weather> weatherList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        weatherList = new ArrayList<>();
        locationAdapter = new LocationAdapter(weatherList);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(locationAdapter);

        SearchView searchView = view.findViewById(R.id.search_location);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Called when the search icon is clicked
                String query = searchView.getQuery().toString();
                LoadLocationData(query);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LoadLocationData(query); // Call the LoadLocationData method with the entered query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return false;
            }
        });
    }

    private void LoadLocationData(String cityName) {
        // Retrofit initialization and API call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService weatherApi = retrofit.create(APIService.class);
        Call<Weather> call = weatherApi.getCurrentWeather(apiKey, cityName);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (response.isSuccessful()) {
                    Weather weather = response.body();
                    if (weather != null) {
                        // Check if the location already exists in the weatherList
                        String locationName = weather.getLocation().getName();
                        boolean isLocationExists = false;
                        for (Weather existingWeather : weatherList) {
                            String existingLocationName = existingWeather.getLocation().getName();
                            if (existingLocationName.equalsIgnoreCase(locationName)) {
                                isLocationExists = true;
                                break;
                            }
                        }
                        if (!isLocationExists) {
                            weatherList.add(weather); // Add the retrieved weather data to the list
                            locationAdapter.notifyDataSetChanged(); // Notify the adapter that the data set has changed
                        }
                    }
                } else {
                    // Handle the unsuccessful response
                    Toast.makeText(getContext(), "Failed to load weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                // Handle the network failure
                Toast.makeText(getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}