package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.agencedevoyage.Dao.UserDao;
import com.example.agencedevoyage.Database.AppDatabase;
import com.example.agencedevoyage.Entity.User;
import com.example.agencedevoyage.R;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signupText;

    private AppDatabase appDatabase;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize the database and DAO
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        userDao = appDatabase.userDao();

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Simple validation
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                // Pass the activity context to the AsyncTask
                new LoginAsyncTask(LoginActivity.this, userDao).execute(username, password);
            }
        });

        // Handle SignUp click
        signupText = findViewById(R.id.signupText);
        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, Registration.class);
            startActivity(intent);
        });

        // Handle Forgot Password click
        TextView forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    // AsyncTask to handle login in the background
    private static class LoginAsyncTask extends AsyncTask<String, Void, User> {
        private UserDao userDao;
        private LoginActivity activity;  // Reference to the LoginActivity

        // Constructor to accept the activity context and DAO
        LoginAsyncTask(LoginActivity activity, UserDao userDao) {
            this.activity = activity;
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            return userDao.getUserByUsernameAndPassword(username, password);
        }

        // Inside onPostExecute in LoginAsyncTask in LoginActivity.java
        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                // Save the user's name to SharedPreferences
                SharedPreferences preferences = activity.getSharedPreferences("user_session", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user_name", user.getName()); // Assuming 'getName()' retrieves the user's name
                editor.apply();

                // Login successful, navigate to MainActivity
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show();
            } else {
                // Show error message
                Toast.makeText(activity, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
