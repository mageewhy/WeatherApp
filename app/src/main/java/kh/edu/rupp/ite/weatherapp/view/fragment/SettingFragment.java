package kh.edu.rupp.ite.weatherapp.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kh.edu.rupp.ite.weatherapp.R;
import kh.edu.rupp.ite.weatherapp.databinding.FragmentSettingBinding;
import kh.edu.rupp.ite.weatherapp.utility.SettingPreference;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;
    private final String[] temp = {"°C", "°F"};
    private final String[] speed = {"Km/h", "M/h"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner tempList = binding.getRoot().findViewById(R.id.tempSpinner);
        ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, temp);
        tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        tempList.setAdapter(tempAdapter);

        Spinner windList = binding.getRoot().findViewById(R.id.windSpinner);
        ArrayAdapter<String> windAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, speed);
        windAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        windList.setAdapter(windAdapter);

        SetSpinnerEvent(tempList, temp);
        SetSpinnerEvent(windList, speed);
    }

    public void SetSpinnerEvent(Spinner spinner, String[] option) {
        if (option == speed) {
            String select = SettingPreference.getInstance(getContext()).getKeyValue("speed");
            if (select.equals("Km/h")) {
                spinner.setSelection(0);
            } else {
                spinner.setSelection(1);
            }
        } else {
            String select = SettingPreference.getInstance(getContext()).getKeyValue("temp");
            if (select.equals("°C")) {
                spinner.setSelection(0);
            } else {
                spinner.setSelection(1);
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (option == temp) {
                    SettingPreference.getInstance(getContext()).storeKey("temp", option[position]);
                } else {
                    SettingPreference.getInstance(getContext()).storeKey("speed", option[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
