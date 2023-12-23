package kh.edu.rupp.ite.weatherapp.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentLocationBinding;
import kh.edu.rupp.ite.weatherapp.ui.adapter.LocationAdapter;
import kh.edu.rupp.ite.weatherapp.viewmodel.WeatherViewModel;


public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private LocationAdapter locationAdapter = new LocationAdapter(new ArrayList<>());
    private WeatherViewModel viewModel;
    private String lastQuery = ""; // Track the last query

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerLayout.setLayoutManager(layoutManager);

        // Setup Recycler View
        binding.recyclerLayout.setAdapter(locationAdapter);

        // Retrieve the ViewModel from the parent activity
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);

        SearchView searchView = view.findViewById(R.id.search_location);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Called when the search icon is clicked
                String query = searchView.getQuery().toString();
                if (!query.equals(lastQuery)) {
                    lastQuery = query;
                    viewModel.LoadLocationData(query);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Set the selected city name in the WeatherViewModel
                if (!query.equals(lastQuery)) {
                    lastQuery = query;
                    viewModel.LoadLocationData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return false;
            }
        });

        // Setup Observer
        viewModel.getWeatherLocationData().observe(getViewLifecycleOwner(), new Observer<ApiData<List<Weather>>>() {
            @Override
            public void onChanged(ApiData<List<Weather>> weatherApiData) {
                switch (weatherApiData.getStatus()) {
                    case PROCESSING:
                        Toast.makeText(getContext(), "Fetching Data", Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        locationAdapter.setWeatherList(weatherApiData.getData());
                        locationAdapter.notifyDataSetChanged();
                        break;
                    case ERROR:
                        Toast.makeText(getContext(), "Network Failed", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

        // Swipe to Refresh Layout
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Get the latest city name from your UI or pass it as an argument
                List<String> cityNames = locationAdapter.getCityNames();

                // Call the refreshLocationData method to refresh the location data
                for (String cityName : cityNames) {
                    viewModel.refreshLocationData(cityName);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}

