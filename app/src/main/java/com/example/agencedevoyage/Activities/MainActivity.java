package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.CategoryAdapter;
import com.example.agencedevoyage.Adapters.PopularAdapter;
import com.example.agencedevoyage.Database.RetrofitClient;
import com.example.agencedevoyage.Domains.CategoryDomain;
import com.example.agencedevoyage.Domains.PopularDomain;
import com.example.agencedevoyage.Entity.ApiService;
import com.example.agencedevoyage.Entity.UserActivity;
import com.example.agencedevoyage.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPop , adapterCat ;
    private RecyclerView recyclerViewPop , recyclerViewCat ;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Retrieve the user's name from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userName = preferences.getString("user_name", "Guest"); // Default to "Guest" if no name is found

        // Set the "Welcome Back" text to include the user's name
        TextView welcomeTextView = findViewById(R.id.textView7); // The TextView with "Welcome Back"
        welcomeTextView.setText( userName);
        apiService = RetrofitClient.getClient().create(ApiService.class);

        initRecyclerView() ;
        logUserAction("login"); // Example action for login

        ImageView logoutButton = findViewById(R.id.nv_logout);

        logoutButton.setOnClickListener(view -> {
            // Display feedback to the user
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

            // Clear any user session (if needed)
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();  // Clear stored session data

            editor.putBoolean("is_logged_in", false); // Reset login state

            editor.apply();  // Save the changes

            // Navigate to LoginActivity (or any other screen)
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear back stack
            startActivity(intent);
            finish();  // Close current activity
        });
        Button analyticsButton = findViewById(R.id.analyticsButton);
        analyticsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AnalyticsActivity.class);
            startActivity(intent);
        });

    }
    private void initRecyclerView(){
        ArrayList<PopularDomain> items = new ArrayList<>() ;


        items.add(new PopularDomain (  "Mar caible, avendia lago", "Miami Beach",  "This 2 bed /1 bath home boasts an enormous,"
                +"open-living plan, accented by striking" +
                "architectural features and high-end finishes." +
                "Feel inspired by open sight lines that" +
                " embrace the outdoors, crowned by stunning" +
                "coffered ceilings. ",  2,  true,  4.8,  "pic1",  true,  1000));

        items.add(new PopularDomain (  "Passo Rolle, TN",  "Hawaii Beach",  "This 2 bed /1 bath home boasts an enormous,"+
                "open-living plan, accented by striking" +
                "architectural features and high-end finishes." +
                "Feel inspired by open sight lines that" +
                " embrace the outdoors, crowned by stunning" +
                "coffered ceilings. ", 1, false,  5,  "pic2",  false,  2500));
        items.add(new PopularDomain (  "Mar caible , avendia lago",  "Miami Beach",  "This 2 bed /1 bath home boasts an enormous,"
                +"open-living plan, accented by striking" +
                "architectural features and high-end finishes." +
                "Feel inspired by open sight lines that" +
                " embrace the outdoors, crowned by stunning" +" coffered ceilings. ",  3,  true,  4.3, "pic3",  true, 30000));

        recyclerViewPop = findViewById(R.id.view_pop);
        recyclerViewPop.setLayoutManager(new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL,  false));
        adapterPop = new PopularAdapter(items) ;
        recyclerViewPop.setAdapter(adapterPop);


        ArrayList<CategoryDomain> catsList =new ArrayList<>();
        catsList.add(new CategoryDomain(  "Beaches",  "cat1"));
        catsList.add(new CategoryDomain(  "Camps",  "cat2"));
        catsList.add(new CategoryDomain(  "Forest",  "cat3"));
        catsList.add(new CategoryDomain(  "Desert",  "cat4"));
        catsList.add(new CategoryDomain(  "Mountain",  "cat5"));
        recyclerViewCat =findViewById(R.id.view_cat);
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(  this, LinearLayoutManager.HORIZONTAL,  false));
        adapterCat=new CategoryAdapter(catsList);
        recyclerViewCat.setAdapter(adapterCat);
    }


    private void logUserAction(String action) {
        // Retrieve userId dynamically from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1); // Default to -1 if not found

        // Only log the action if a valid userId is found
        if (userId == -1) {
            Log.e("LogUserAction", "User ID not found or invalid.");
            Toast.makeText(this, "User ID not found. Unable to log activity.", Toast.LENGTH_SHORT).show();
            return;  // Early return if userId is invalid
        }

        // Format the timestamp to match the ISO 8601 format (e.g., 2024-11-12T19:58:19Z)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(new Date());

        // Create the UserActivity object to log the action
        UserActivity activity = new UserActivity(userId, action, formattedDate);

        // Make the API call to log the activity
        apiService.logUserActivity(activity).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Show success message if the activity was logged successfully
                    Toast.makeText(MainActivity.this, "Activity logged", Toast.LENGTH_SHORT).show();
                } else {
                    // Log the response code and error body for debugging
                    Log.e("LogUserAction", "Failed to log activity. Response code: " + response.code());
                    try {
                        String errorBody = response.errorBody().string();  // Get the error body as a string
                        Log.e("LogUserAction", "Error Body: " + errorBody);
                        Toast.makeText(MainActivity.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("LogUserAction", "Error reading the error body", e);
                        Toast.makeText(MainActivity.this, "Failed to read error message", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log the error with Log.e instead of printStackTrace
                Log.e("LogUserAction", "Network error", t);
                // Show network error message to the user
                Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}