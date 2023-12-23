    package kh.edu.rupp.ite.weatherapp.ui.adapter;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;


    import java.util.List;

    import kh.edu.rupp.ite.weatherapp.R;
    import kh.edu.rupp.ite.weatherapp.api.model.Current;
    import kh.edu.rupp.ite.weatherapp.api.model.Location;
    import kh.edu.rupp.ite.weatherapp.api.model.Weather;

    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
        private List<Weather> weatherList;

        public LocationAdapter(List<Weather> weatherList) {
            this.weatherList = weatherList;
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

            holder.cityNameTextView.setText(location.getName());
            holder.countryNameTextView.setText(location.getCountry());
            holder.dateTimeZone.setText(location.getLocaltime());
            holder.tempView.setText(String.format("%.1f°C", current.getTemp_c()));
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

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                cityNameTextView = itemView.findViewById(R.id.city_name);
                countryNameTextView = itemView.findViewById(R.id.country_name);
                dateTimeZone = itemView.findViewById(R.id.date_timezone);
                tempView = itemView.findViewById(R.id.temp_view);

            }
        }
    }