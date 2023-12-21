package kh.edu.rupp.ite.weatherapp.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.api.model.Location;
import kh.edu.rupp.ite.weatherapp.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.api.service.APIService;
import kh.edu.rupp.ite.weatherapp.api.service.LocationRepo;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentLocationBinding;
import kh.edu.rupp.ite.weatherapp.ui.adapter.LocationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private LocationAdapter adapter;
    private EditText searchEditText;

    LocationRepo locationRepo = new LocationRepo();
    String apiKey = "25714936cd1e401ea1270735231610";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupSearchView();
    }

    private void setupRecyclerView() {
        List<Weather> weatherList = new ArrayList<>(); // Modify this to get the weather data
        adapter = new LocationAdapter(weatherList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerLayout.setLayoutManager(layoutManager);

        binding.recyclerLayout.setAdapter(adapter);
    }

    private void loadWeatherData(String query) {
        locationRepo.loadWeather(new LocationRepo.LoadWeatherCallback() {
            @Override
            public void onWeatherLoaded(Weather weather) {
                // Update the weather list in the adapter
                adapter.setWeatherList(Collections.singletonList(weather));
                // Notify the adapter of the data change
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onWeatherLoadFailed() {
                // Handle the error when weather data loading fails
                Toast.makeText(getContext(), "Failed to load weather data", Toast.LENGTH_SHORT).show();
            }
        }, apiKey, query);
    }

    private void setupSearchView() {
        searchEditText = binding.searchSrcText;
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchEditText.getText().toString();
                loadWeatherData(query);
                return true;
            }
            return false;
        });
    }
}