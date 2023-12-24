    package kh.edu.rupp.ite.weatherapp.ui.adapter;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.ItemTouchHelper;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.ArrayList;
    import java.util.List;

    import kh.edu.rupp.ite.weatherapp.R;
    import kh.edu.rupp.ite.weatherapp.model.api.model.Current;
    import kh.edu.rupp.ite.weatherapp.model.api.model.Location;
    import kh.edu.rupp.ite.weatherapp.model.api.model.Weather;
    import kh.edu.rupp.ite.weatherapp.utility.SettingPreference;

    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
        private List<Weather> weatherList;
        private String temp;
        SettingPreference settingPreference;

        public LocationAdapter(List<Weather> weatherList, Context mContext) {
            this.weatherList = weatherList;
            this.mContext = mContext;
        }
        public void setWeatherList(List<Weather> weatherList) {
            this.weatherList = weatherList;
        }
        public List<String> getCityNames() {
            List<String> cityNames = new ArrayList<>();
            for (Weather weather : weatherList) {
                Location location = weather.getLocation();
                cityNames.add(location.getName());
            }
            return cityNames;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Weather weather = weatherList.get(position);
            Location location = weather.getLocation();
            Current current = weather.getCurrent();
//            Condition condition = weather.getCondition();

            // Get the SharedPreferences instance
            settingPreference = SettingPreference.getInstance(holder.itemView.getContext());
            this.temp = settingPreference.getKeyValue("temp");

            holder.cityNameTextView.setText(location.getName());
            holder.countryNameTextView.setText(location.getCountry());
            holder.dateTimeZone.setText(location.getLocaltime());
//            Picasso.get()
//                    .load(condition.getIcon())
//                    .into(holder.iconImageView);
            if (this.temp.equals("°C")) {
                holder.tempView.setText(String.format("%.1f°C", current.getTemp_c()));
            } else {
                holder.tempView.setText(String.format("%.1f°F", current.getTemp_f()));
            }
        }

        @Override
        public int getItemCount() {
            return weatherList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView cityNameTextView;
            TextView countryNameTextView;
            TextView dateTimeZone;
            TextView tempView;
            ImageView iconImageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cityNameTextView = itemView.findViewById(R.id.city_name);
                countryNameTextView = itemView.findViewById(R.id.country_name);
                dateTimeZone = itemView.findViewById(R.id.date_timezone);
                iconImageView = itemView.findViewById(R.id.dynamic_icon_rcv);
                tempView = itemView.findViewById(R.id.temp_view);

            }
        }

    }