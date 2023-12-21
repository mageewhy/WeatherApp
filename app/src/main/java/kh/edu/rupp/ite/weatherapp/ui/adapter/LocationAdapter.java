    package kh.edu.rupp.ite.weatherapp.ui.adapter;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import java.util.List;

    import kh.edu.rupp.ite.weatherapp.R;
    import kh.edu.rupp.ite.weatherapp.api.model.Weather;
    import kh.edu.rupp.ite.weatherapp.api.service.LocationRepo;

    public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

        private List<Weather> weatherList;
        private LocationRepo locationRepo;

        public LocationAdapter(List<Weather> weatherList) {
            this.weatherList = weatherList;
            this.locationRepo = locationRepo;
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
            // Bind weather data to UI elements in recycler_item.xml
            holder.locationNameTextView.setText(weather.getLocation().getName());
            holder.countryNameTextView.setText(weather.getLocation().getCountry());
//            holder.dateTimeZoneTextView.setText(weather.getDateTimeZone());
//            holder.temperatureTextView.setText(weather.getTemperature());
            // ...
        }

        @Override
        public int getItemCount() {
            return weatherList.size();
        }

        public void setWeatherList(List<Weather> weathers) {
            weatherList.clear(); // Clear the existing list
            weatherList.addAll(weathers); // Add all the new weather items
            notifyDataSetChanged(); // Notify the adapter of the data change
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView locationNameTextView;
            TextView countryNameTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                locationNameTextView = itemView.findViewById(R.id.location_name);
                countryNameTextView = itemView.findViewById(R.id.country_name);
            }
        }
    }