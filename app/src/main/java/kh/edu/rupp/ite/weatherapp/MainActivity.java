package kh.edu.rupp.ite.weatherapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Calendar;

import kh.edu.rupp.ite.weatherapp.databinding.ActivityMainBinding;
import kh.edu.rupp.ite.weatherapp.ui.fragment.HomeFragment;
import kh.edu.rupp.ite.weatherapp.ui.fragment.LocationFragment;
import kh.edu.rupp.ite.weatherapp.ui.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.bottomNavigationView.setSelectedItemId(R.id.menuHome);
        SetFragment(new HomeFragment());

        // Get the current hour of the day
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Set the appropriate background based on the time of day
        if (hourOfDay >= 6 && hourOfDay < 12) {
            // Morning
            findViewById(android.R.id.content).setBackgroundResource(R.drawable.morning_bg);
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            // Afternoon
            findViewById(android.R.id.content).setBackgroundResource(R.drawable.afternoon_bg);
        } else if (hourOfDay >= 18 && hourOfDay < 21) {
            // Evening
            findViewById(android.R.id.content).setBackgroundResource(R.drawable.evening_bg);
        } else {
            // Night
            findViewById(android.R.id.content).setBackgroundResource(R.drawable.night_bg);
        }

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menuHome) {
                SetFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.menuLocation) {
                SetFragment(new LocationFragment());
            } else {
                SetFragment(new SettingFragment());
            }
            return true;
        });

    }

    private void SetFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.layoutFragment, fragment);
        fragmentTransaction.commit();
    }
}
