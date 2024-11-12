package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.CategoryAdapter;
import com.example.agencedevoyage.Adapters.PopularAdapter;
import com.example.agencedevoyage.Domains.CategoryDomain;
import com.example.agencedevoyage.Domains.PopularDomain;
import com.example.agencedevoyage.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPop, adapterCat;
    private RecyclerView recyclerViewPop, recyclerViewCat;
    private ImageView notificationIcon; // Changed from Button to ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize notification icon (ImageView)
        notificationIcon = findViewById(R.id.imageView4); // Ensure ID matches XML layout
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Main_rahmaActivity.class);
            startActivity(intent);
        });

        // Retrieve the user's name from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userName = preferences.getString("user_name", "Guest");

        // Set the "Welcome Back" text with the user's name
        TextView welcomeTextView = findViewById(R.id.textView7);
        welcomeTextView.setText(userName);

        // Initialize RecyclerViews
        recyclerViewPop = findViewById(R.id.view_pop);
        recyclerViewCat = findViewById(R.id.view_cat);
        initRecyclerView();

        // Logout button logic
        ImageView logoutButton = findViewById(R.id.nv_logout);
        logoutButton.setOnClickListener(view -> {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.putBoolean("is_logged_in", false);
            editor.apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initRecyclerView() {
        ArrayList<PopularDomain> items = new ArrayList<>();
        items.add(new PopularDomain("Mar caible, avendia lago", "Miami Beach", "Description...", 2, true, 4.8, "pic1", true, 1000));
        items.add(new PopularDomain("Passo Rolle, TN", "Hawaii Beach", "Description...", 1, false, 5, "pic2", false, 2500));

        recyclerViewPop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterPop = new PopularAdapter(items);
        recyclerViewPop.setAdapter(adapterPop);

        ArrayList<CategoryDomain> catsList = new ArrayList<>();
        catsList.add(new CategoryDomain("Beaches", "cat1"));
        catsList.add(new CategoryDomain("Camps", "cat2"));

        recyclerViewCat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterCat = new CategoryAdapter(catsList);
        recyclerViewCat.setAdapter(adapterCat);
    }
}
