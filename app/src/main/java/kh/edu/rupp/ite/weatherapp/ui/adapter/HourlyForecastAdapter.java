package kh.edu.rupp.ite.weatherapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kh.edu.rupp.ite.weatherapp.databinding.ViewHolderHourlyForecastBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.Forecast;
import kh.edu.rupp.ite.weatherapp.model.api.model.Hour;
import kh.edu.rupp.ite.weatherapp.utility.SettingPreference;

public class HourlyForecastAdapter extends ListAdapter<Hour, HourlyForecastAdapter.HourlyForecastViewHolder> {

    private SettingPreference settingPreference;
    public HourlyForecastAdapter() {
        super(new DiffUtil.ItemCallback<Hour>() {
            @Override
            public boolean areItemsTheSame(@NonNull Hour oldItem, @NonNull Hour newItem) {
                return oldItem == newItem;
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
        HourlyForecastViewHolder viewHolder = new HourlyForecastViewHolder(binding);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastViewHolder holder, int position) {

        Hour hour = getItem(position);
        holder.bind(hour, settingPreference);

    }

    public static class HourlyForecastViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderHourlyForecastBinding itemBinding;

        public HourlyForecastViewHolder(ViewHolderHourlyForecastBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind(Hour hour, SettingPreference settingPreference) {
            LocalDateTime dateTime = LocalDateTime.parse(hour.getTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String timeString = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            // Get the SharedPreferences instance
            settingPreference = SettingPreference.getInstance(itemBinding.getRoot().getContext());
            String temp = settingPreference.getKeyValue("temp");

            itemBinding.txt1.setText(timeString);
            Picasso.get().load(hour.getCondition().getIcon()).into(itemBinding.img1);
            if (temp.equals("°C")) {
                itemBinding.txt2.setText(String.format("%.1f°C", hour.getTemp_c()));
            } else {
                itemBinding.txt2.setText(String.format("%.1f°F", hour.getTemp_f()));
            }

        }

    }

}