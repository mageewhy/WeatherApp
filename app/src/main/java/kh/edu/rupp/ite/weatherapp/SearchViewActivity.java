package kh.edu.rupp.ite.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.ite.weatherapp.api.model.Location;
import kh.edu.rupp.ite.weatherapp.api.service.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchViewActivity extends AppCompatActivity {

    private AutoCompleteTextView searchAutoComplete;
    private ImageButton searchButton;
    private String apiKey = "25714936cd1e401ea1270735231610";
    private List<Location> masterList = new ArrayList<>();
    private ArrayAdapter<Location> adapter;
    List<Location> filtered = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view_layout);

        // Initialize views
        searchAutoComplete = findViewById(R.id.searchAutoComplete);
        searchButton = findViewById(R.id.searchButton);

        // Set click listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });

        // Set up autocomplete suggestions
        setupAutoComplete();
    }

    private void setupAutoComplete() {
        // Create an instance of your API service
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = httpClient.create(APIService.class);

        // Set up adapter for autocomplete suggestions
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setAdapter(adapter);

        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Get the search query from the AutoCompleteTextView
                String query = s.toString().trim();

                // Make the API call to fetch the suggestions
                Call<ArrayList<Location>> call = apiService.LoadLocationCity(apiKey, query);

                call.enqueue(new Callback<ArrayList<Location>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Location>> call, Response<ArrayList<Location>> response) {
                        String lowerQuery = query.toLowerCase();

                        if(response.isSuccessful()) {

                            ArrayList<Location> suggestions = response.body();

                            // Add to master list
                            masterList.addAll(suggestions);
                            Log.d("Master List", masterList.toString());

                            // Filter list

                            for(Location loc: masterList) {
                                Log.d("Loc Name", loc.getName());
                                if(loc.getName().toLowerCase().contains(lowerQuery)){
                                    // Create new Location with match data
                                    Location match = new Location();
                                    match.setName(loc.getName());
                                    match.setCountry(loc.getCountry());

                                    // Add to filtered
                                    filtered.add(match);
                                }
                            }

                            // Update adapter
                            adapter.clear();
                            adapter.addAll(filtered);
                            adapter.notifyDataSetChanged();

                            Log.d("Adapter List", filtered.toString());

                        } else {
                            // handle error
                            Toast.makeText(SearchViewActivity.this, "Request Failed", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Location>> call, Throwable t) {
                        // Handle API call failure
                        Toast.makeText(SearchViewActivity.this, "Request Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not used
            }
        });
    }

    private void performSearch() {
        // Get the search query from the AutoCompleteTextView
        String query = searchAutoComplete.getText().toString().trim();

        // Pass the search query back to the previous screen
        Intent resultIntent = new Intent();
        resultIntent.putExtra("search_query", query);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}