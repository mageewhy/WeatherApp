package kh.edu.rupp.ite.weatherapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.databinding.RecylerItemBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.Current;
import kh.edu.rupp.ite.weatherapp.model.api.model.Location;
import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
import kh.edu.rupp.ite.weatherapp.utility.SettingPreference;

public class LocationAdapter extends ListAdapter<Weather, LocationAdapter.LocationViewHolder> {
    SettingPreference settingPreference;

    public LocationAdapter() {
        super(new DiffUtil.ItemCallback<Weather>() {
            @Override
            public boolean areItemsTheSame(@NonNull Weather oldItem, @NonNull Weather newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Weather oldItem, @NonNull Weather newItem) {
                return oldItem.getLocation().getName().equals(newItem.getLocation().getName());
            }
        });
    }

    public void setWeatherList(List<Weather> weatherList) {
        submitList(weatherList);
        notifyDataSetChanged();
    }
    public List<String> getCityNames() {
        List<String> cityNames = new ArrayList<>();
        for (Weather weather : getCurrentList()) {
            Location location = weather.getLocation();
            cityNames.add(location.getName());
        }
        return cityNames;
    }
    @Override
    public long getItemId(int position) {
        // Return a unique ID for each item based on its position
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecylerItemBinding binding = RecylerItemBinding.inflate(layoutInflater, parent, false);
        return new LocationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Weather weather = getCurrentList().get(position);
        holder.bind(weather, settingPreference);
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        private final RecylerItemBinding itemBinding;

        public LocationViewHolder(RecylerItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(Weather weather, SettingPreference settingPreference) {
            Location location = weather.getLocation();
            Current current = weather.getCurrent();

            itemBinding.cityName.setText(location.getName());
            itemBinding.countryName.setText(location.getCountry());
            itemBinding.dateTimezone.setText(location.getLocaltime());
            Picasso.get().load(weather.getCurrent().getCondition().getIcon()).into(itemBinding.dynamicIconRcv);
            // Adjust temperature based on settings
            settingPreference = SettingPreference.getInstance(itemView.getContext());
            String temp = settingPreference.getKeyValue("temp");
            if (temp.equals("°C")) {
                itemBinding.tempView.setText(String.format("%.1f°C", current.getTemp_c()));
            } else {
                itemBinding.tempView.setText(String.format("%.1f°F", current.getTemp_f()));
            }
        }
    }
}
