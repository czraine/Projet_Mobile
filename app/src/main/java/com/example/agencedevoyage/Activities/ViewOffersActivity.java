package com.example.agencedevoyage.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agencedevoyage.Adapters.OfferAdapter;
import com.example.agencedevoyage.Database.AppDatabase_firas;
import com.example.agencedevoyage.Entity.Offer;
import com.example.agencedevoyage.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewOffersActivity extends AppCompatActivity {

    private RecyclerView offersRecyclerView;
    private OfferAdapter offerAdapter;
    private AppDatabase_firas offerDatabase;
    private ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    private EditText searchEditText, destinationEditText;
    private SeekBar priceRangeSeekBar;
    private Spinner typeSpinner;
    private Button dateButton;
    private long selectedDateInMillis = 0;
    private static final int EDIT_OFFER_REQUEST_CODE = 1001;  // Define this constant

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offers);

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        priceRangeSeekBar = findViewById(R.id.priceRangeSeekBar);
        typeSpinner = findViewById(R.id.typeSpinner);
        dateButton = findViewById(R.id.dateButton);
        offersRecyclerView = findViewById(R.id.offersRecyclerView);

        offersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        offerDatabase = AppDatabase_firas.getInstance(this);

        setupTypeSpinner(); // Populate spinner with types
        setupFilters();
        loadOffersFromDatabase();
    }

    private void setupTypeSpinner() {
        List<String> types = Arrays.asList("All", "Adventure", "Family", "Luxury", "Budget");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Trigger filter when a type is selected
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterOffers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupFilters() {
        // Date picker for date filter
        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(ViewOffersActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        selectedDateInMillis = calendar.getTimeInMillis();
                        filterOffers();
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // SeekBar for price range filtering
        priceRangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filterOffers();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Search text-based filtering
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOffers();
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void filterOffers() {
        String destination = destinationEditText.getText().toString().trim();
        double maxPrice = priceRangeSeekBar.getProgress();
        String type = typeSpinner.getSelectedItem().toString().equals("All") ? null : typeSpinner.getSelectedItem().toString();
        String searchQuery = searchEditText.getText().toString().trim();

        databaseExecutor.execute(() -> {
            List<Offer> filteredOffers = offerDatabase.offerDao()
                    .filterOffers(
                            destination.isEmpty() ? null : destination,
                            maxPrice > 0 ? maxPrice : 0,
                            type,  // Use `type` directly here, which will be null if "All" is selected
                            selectedDateInMillis > 0 ? selectedDateInMillis : 0,
                            searchQuery.isEmpty() ? null : "%" + searchQuery + "%"
                    );

            runOnUiThread(() -> offerAdapter.updateOffersList(filteredOffers));
        });
    }


    private void loadOffersFromDatabase() {
        databaseExecutor.execute(() -> {
            List<Offer> offersList = offerDatabase.offerDao().getAllOffers();
            runOnUiThread(() -> {
                // Convert offersList to ArrayList before passing it to OfferAdapter
                offerAdapter = new OfferAdapter(ViewOffersActivity.this, new ArrayList<>(offersList));
                offersRecyclerView.setAdapter(offerAdapter);
            });
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_OFFER_REQUEST_CODE && data != null) {
            Offer updatedOffer = (Offer) data.getSerializableExtra("updatedOffer");
            if (updatedOffer != null) {
                for (int i = 0; i < offerAdapter.getItemCount(); i++) {
                    if (offerAdapter.getOffersList().get(i).getId() == updatedOffer.getId()) {
                        offerAdapter.getOffersList().set(i, updatedOffer);
                        offerAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    }
}
