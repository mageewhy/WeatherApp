package kh.edu.rupp.ite.weatherapp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentLocationBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.ui.adapter.LocationAdapter;
import kh.edu.rupp.ite.weatherapp.utility.WeatherPreference;
import kh.edu.rupp.ite.weatherapp.viewmodel.WeatherViewModel;


public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private final LocationAdapter locationAdapter = new LocationAdapter();
    private WeatherViewModel viewModel;
    private WeatherPreference weatherPreference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        // Setup Recycler View
        binding.recyclerLayout.setAdapter(locationAdapter);

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
        // Use the 'allWeatherData' list containing all the retrieved Weather objects as needed
        List<Weather> allWeatherData = viewModel.getAllWeatherDataFromSharedPreferences(requireContext());
        viewModel.getAllWeatherDataFromSharedPreferences(getContext());

        // Set the retrieved weather data into the adapter
        locationAdapter.setWeatherList(allWeatherData);
        locationAdapter.notifyDataSetChanged();

        // Method to create item touch helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                String cityRemoved = locationAdapter.getCityNames().remove(position);
                locationAdapter.notifyItemRemoved(position);

                // Remove from SharedPreferences and update ViewModel
                viewModel.removeWeatherDataFromSharedPreferences(requireContext(), cityRemoved);

                viewModel.refreshLocationData(requireContext());
            }
            // Add to recycler view.
        });
        itemTouchHelper.attachToRecyclerView(binding.recyclerLayout);

        SearchView searchView = view.findViewById(R.id.search_location);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Called when the search icon is clicked
                String query = searchView.getQuery().toString();
                viewModel.LoadLocationData(requireContext(), query);
                // Call the refreshLocationData method to refresh the location data
                viewModel.refreshLocationData(requireContext());
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                weatherPreference = WeatherPreference.getInstance(getContext());
                if(weatherPreference.getAllKeys().contains(query)){
                    Toast.makeText(getContext(), "City Already Exists", Toast.LENGTH_LONG).show();
                }
                else {
                    //Call LoadLocationData to call API
                    viewModel.LoadLocationData(getContext(), query);
                }
                // Call the refreshLocationData method to refresh the location data
                viewModel.refreshLocationData(requireContext());

                // Close the keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);

                // Clear the query in the SearchView
                searchView.setQuery("", false);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return false;
            }
        });

        // Observing LiveData for weather location data
        viewModel.getWeatherLocationData().observe(getViewLifecycleOwner(), new Observer<ApiData<List<Weather>>>() {
            @Override
            public void onChanged(ApiData<List<Weather>> weatherApiData) {
                // Set Observer
                int delayMillis = 1000;
                switch (weatherApiData.getStatus()) {
                    case PROCESSING:
                        binding.progressBar.setVisibility(View.VISIBLE); // Show the progress bar
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.VISIBLE); // Show the progress bar
                        new Handler().postDelayed(() -> {
                            // Hide the progress bar after the delay
                            binding.progressBar.setVisibility(View.GONE);
                        }, delayMillis);
                        locationAdapter.setWeatherList(weatherApiData.getData());
                        locationAdapter.notifyDataSetChanged();

                        // Log the received data
                        if (weatherApiData.getData() != null) {
                            for (Weather weather : weatherApiData.getData()) {
                                if (weather != null && weather.getLocation() != null) {
                                    Log.d("LiveDataReceivedData", "Live Data City Name: " + weather.getLocation().getName());
                                    Log.d("LiveDataReceivedData", "Live Data Current Time: " + weather.getLocation().getLocaltime());
                                }
                            }
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE); // Hide the progress bar on error
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
                // Call the refreshLocationData method to refresh the location data
                viewModel.refreshLocationData(requireContext());

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}

