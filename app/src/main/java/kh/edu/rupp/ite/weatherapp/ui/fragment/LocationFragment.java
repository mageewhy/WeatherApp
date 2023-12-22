package kh.edu.rupp.ite.weatherapp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.SearchViewActivity;
import kh.edu.rupp.ite.weatherapp.api.service.LocationRepo;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentLocationBinding;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;

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

        // Get a reference to the Add_Location ImageButton
        ImageButton addLocationButton = view.findViewById(R.id.Add_Location);

        // Set an OnClickListener for the button
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the search view layout
                openSearchView();
            }
        });
    }

    // Method to open the search view layout
    private void openSearchView() {
        // Create an Intent to navigate to the search view activity or fragment
        Intent intent = new Intent(requireContext(), SearchViewActivity.class);
        startActivity(intent);
    }

    private void LoadLocationData(String query) {
        // Implement your code to load location data based on the query
    }
}