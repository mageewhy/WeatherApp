package kh.edu.rupp.ite.weatherapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import kh.edu.rupp.ite.weatherapp.databinding.ViewHolderHourlyForecastBinding;
import kh.edu.rupp.ite.weatherapp.model.api.model.Forecast;
import kh.edu.rupp.ite.weatherapp.model.api.model.Hour;

public class HourlyForecastAdapter extends ListAdapter<Forecast, HourlyForecastAdapter.HourlyForecastViewHolder> {

    public HourlyForecastAdapter() {
        super(new DiffUtil.ItemCallback<Hour>() {
            @Override
            public boolean areItemsTheSame(@NonNull Hour oldItem, @NonNull Hour newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Hour oldItem, @NonNull Forecast newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    @NonNull
    @Override
    public HourlyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewHolderHourlyForecastBinding binding = ViewHolderHourlyForecastBinding.inflate(layoutInflater, parent, false);
        HourlyForecastViewHolder viewHolder = new HourlyForecastViewHolder(binding.getRoot());
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastViewHolder holder, int position) {

        Forecast item = getItem(position);
        holder.bind(item);

    }

    public static class HourlyForecastViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderHourlyForecastBinding itemBinding;

        public HourlyForecastViewHolder(LinearLayout itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        public void bind1(Hour hour) {
            itemBinding.txt1.setText((hour.getTime());
        }

        public void bind2(Hour hour) {
            Picasso.get().load(hour.getCondition().getIcon()).into(itemBinding.img1);
        }

        public void bind3(Hour hour) {
            itemBinding.txt2.setText(Float.toString(hour.getTemp_c()));
        }

    }

}