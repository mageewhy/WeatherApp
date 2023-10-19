package kh.edu.rupp.ite.weatherapp;


import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import kh.edu.rupp.ite.weatherapp.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    String APIKey = "25714936cd1e401ea1270735231610";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_WeatherApp);
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        Toolbar toolbar = binding.toolbar;
//        setSupportActionBar(toolbar);
//        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
//        toolBarLayout.setTitle(getTitle());

    }
}
