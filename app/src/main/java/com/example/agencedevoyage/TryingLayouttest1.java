package com.example.agencedevoyage;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TryingLayouttest1 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trying_layouttest1);

        // Set up the BottomNavigationView and its listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    replaceFragment(new HomeFragment());
                    Log.d("MainActivity", "HomeFragment loaded");
                } else if (id == R.id.shorts) {
                    replaceFragment(new AboutFragment());
                    Log.d("MainActivity", "AboutFragment loaded");
                } else if (id == R.id.subscriptions) {
                    replaceFragment(new SettingsFragment());
                    Log.d("MainActivity", "SettingsFragment loaded");
                } else if (id == R.id.library) {
                    replaceFragment(new ShareFragment());
                    Log.d("MainActivity", "ShareFragment loaded");
                }

                return true;
            }
        });

        // Load the default fragment when the activity is created
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
            Log.d("MainActivity", "HomeFragment Started");
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the fragment in the FrameLayout
        fragmentTransaction.replace(R.id.frame_layout, fragment);

        // Commit the transaction
        fragmentTransaction.commit();
    }
}
