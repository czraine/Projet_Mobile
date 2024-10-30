package com.example.agencedevoyage.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agencedevoyage.R;
import android.util.Log;
// Import for networking and JSON handling if using an HTTP client (e.g., OkHttp, Retrofit, or Volley)

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;
    private TextView backToLogin;
    private String confirmationCode; // Store the generated confirmation code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.emailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        backToLogin = findViewById(R.id.backToLogin);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Generate a confirmation code
                confirmationCode = String.valueOf((int) ((Math.random() * 9000) + 1000)); // Generate a 4-digit code

                // Call backend to send email with the confirmation code
                sendConfirmationCode(email, confirmationCode);
            }
        });

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void sendConfirmationCode(String email, String code) {
        // Replace with your backend call to send an email
        Log.d("ForgotPassword", "Confirmation code: " + code); // Log the code for testing
        // After sending, navigate to the verification screen
        Intent intent = new Intent(ForgotPasswordActivity.this, ConfirmCodeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", code);
        startActivity(intent);
        finish();
    }
}
