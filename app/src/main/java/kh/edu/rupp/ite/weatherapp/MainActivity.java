package kh.edu.rupp.ite.weatherapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

//        if(timeOfDay >= 8 && timeOfDay < 16){
//
//            //day
//
//        }else if(timeOfDay >= 16 && timeOfDay < 20){
//
//            //afternoon
//
//        }else if(timeOfDay >= 20 && timeOfDay < 24){
//
//            //night
//
//        }else if(timeOfDay>=0 && timeOfDay<4){
//            //night
//        }
//
//        else if(timeOfDay >= 4 && timeOfDay < 8){
//
//            //dawn
//
//        }

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
