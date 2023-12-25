package kh.edu.rupp.ite.weatherapp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentLocationBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.ApiData;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.ui.adapter.LocationAdapter;
import kh.edu.rupp.ite.weatherapp.viewmodel.WeatherViewModel;


public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;
    private final LocationAdapter locationAdapter = new LocationAdapter(new ArrayList<>());
    private WeatherViewModel viewModel;
    private final List<String> cityNames = new ArrayList<>();
    private final String lastQuery = ""; // Track the last query
    private Parcelable recyclerViewState;
    private LinearLayoutManager layoutManager;

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
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerLayout.setLayoutManager(layoutManager);

        // Setup Recycler View
        binding.recyclerLayout.setAdapter(locationAdapter);

        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Get position of item
                int position = viewHolder.getAdapterPosition();

                // remove cityNames from list
                String cityRemoved = locationAdapter.getCityNames().remove(position);
//                Log.d("Removal", "Removed Item: " + cityRemoved);

                // below line is to notify our item is removed from adapter.
                locationAdapter.notifyItemRemoved(position);

                // below line is to remove item from our array list.
                viewModel.removeWeatherDataFromSharedPreferences(getContext(), cityRemoved);

                // Update the adapter's data after the removal
                viewModel.getAllWeatherDataFromSharedPreferences(requireContext()); // Refresh data in ViewModel

                // Call the refreshLocationData method to refresh the location data
                viewModel.refreshLocationData(requireContext());
            }
            // Add to recycler view.
        }).attachToRecyclerView(binding.recyclerLayout);

        // Retrieve the ViewModel from the parent activity
        viewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);

        // Use the 'allWeatherData' list containing all the retrieved Weather objects as needed
        List<Weather> allWeatherData = viewModel.getAllWeatherDataFromSharedPreferences(requireContext());
        viewModel.getAllWeatherDataFromSharedPreferences(getContext());

        // Set the retrieved weather data into the adapter
        locationAdapter.setWeatherList(allWeatherData);
        locationAdapter.notifyDataSetChanged();

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
                // Set the selected city name in the WeatherViewModel
                viewModel.LoadLocationData(requireContext(), query);
                // Call the refreshLocationData method to refresh the location data
                viewModel.refreshLocationData(requireContext());

                // Close the keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);

                // Clear the query in the SearchView
                searchView.setQuery("", false);

                // Collapse the SearchView
                searchView.onActionViewCollapsed();
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
                // Set a delay (in milliseconds) after which the progress bar will be hidden
                int delayMillis = 1000;
                switch (weatherApiData.getStatus()) {
                    case PROCESSING:
//                        Toast.makeText(getContext(), "Fetching Data", Toast.LENGTH_LONG).show();
                        binding.progressBar.setVisibility(View.VISIBLE); // Show the progress bar

                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE); // Hide the progress bar
                        locationAdapter.setWeatherList(weatherApiData.getData());
                        locationAdapter.notifyDataSetChanged();
                        break;
                    case REFRESHING:
//                        Toast.makeText(getContext(), "Refreshing Data", Toast.LENGTH_LONG).show();
                        binding.progressBar.setVisibility(View.VISIBLE); // Show the progress bar
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Hide the progress bar after the delay
                                binding.progressBar.setVisibility(View.GONE);
                            }
                        }, delayMillis);

                        locationAdapter.setWeatherList(weatherApiData.getData());
                        locationAdapter.notifyDataSetChanged();

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
                // Save the current RecyclerView state
                recyclerViewState = layoutManager.onSaveInstanceState();

                // Get the latest city name from your UI or pass it as an argument
                locationAdapter.getCityNames();

                // Call the refreshLocationData method to refresh the location data
                viewModel.refreshLocationData(requireContext());

                // Once the refresh is done, restore the RecyclerView state
                if (recyclerViewState != null) {
                    layoutManager.onRestoreInstanceState(recyclerViewState);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}

