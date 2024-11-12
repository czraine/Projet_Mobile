package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agencedevoyage.R;

public class DashboardActivity extends AppCompatActivity {

    private Button addOfferButton;
    private Button viewOffersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize the buttons
        addOfferButton = findViewById(R.id.addOfferButton);
        viewOffersButton = findViewById(R.id.viewOffersButton);

        // Set a click listener on the add offer button
        addOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, Add_Offres.class);
                startActivity(intent);
            }
        });

        // Set a click listener on the view offers button
        viewOffersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ViewOffersActivity.class);
                startActivity(intent);
            }
        });
    }
}
