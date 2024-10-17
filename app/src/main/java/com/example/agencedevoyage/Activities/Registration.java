package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.agencedevoyage.R;

public class Registration extends AppCompatActivity {
    private ViewPager2 viewPager;
    private RegistrationPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new RegistrationPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        // Get the TextView by ID and set a click listener
        TextView loginText = findViewById(com.example.agencedevoyage.R.id.loginText);
        loginText.setOnClickListener(view -> {
            // Redirect to MainActivity (Login screen)
            Intent intent = new Intent(Registration.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optional: closes the SignUpActivity so it can't be returned to
        });
    }
}