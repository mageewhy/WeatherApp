package kh.edu.rupp.ite.weatherapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.databinding.ViewHolderHourlyForecastBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.Hour;
import kh.edu.rupp.ite.weatherapp.utility.SettingPreference;

public class HourlyForecastAdapter extends ListAdapter<Hour, HourlyForecastAdapter.HourlyForecastViewHolder> {

    private SettingPreference settingPreference;
    private LocalDateTime currentTime;

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public HourlyForecastAdapter() {
        super(new DiffUtil.ItemCallback<Hour>() {
            @Override
            public boolean areItemsTheSame(@NonNull Hour oldItem, @NonNull Hour newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Hour oldItem, @NonNull Hour newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    @NonNull
    @Override
    public HourlyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderHourlyForecastBinding binding = ViewHolderHourlyForecastBinding.inflate(layoutInflater, parent, false);
        return new HourlyForecastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastViewHolder holder, int position) {
        Hour hour = getItem(position);
        if (position == 0 && currentTime != null) {
            holder.bindCurrentTime(currentTime);
        } else {
            holder.bind(hour, settingPreference);
        }
    }

    public static class HourlyForecastViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderHourlyForecastBinding itemBinding;

        public HourlyForecastViewHolder(ViewHolderHourlyForecastBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bindCurrentTime(LocalDateTime currentTime) {
            String timeString = currentTime.format(DateTimeFormatter.ofPattern("hh a"));
            itemBinding.time.setText(timeString);
            // Additional logic for displaying the current time if needed
        }

        public void bind(Hour hour, SettingPreference settingPreference) {
            LocalDateTime dateTime = LocalDateTime.parse(hour.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String timeString = dateTime.format(DateTimeFormatter.ofPattern("hh a"));

            settingPreference = SettingPreference.getInstance(itemBinding.getRoot().getContext());
            String temp = settingPreference.getKeyValue("temp");

            itemBinding.time.setText(timeString);
            Picasso.get().load(hour.getCondition().getIcon()).into(itemBinding.img1);
            if (temp.equals("°C")) {
                itemBinding.degree.setText(String.format("%.1f°C", hour.getTemp_c()));
            } else {
                itemBinding.degree.setText(String.format("%.1f°F", hour.getTemp_f()));
            }
        }


    }
}
