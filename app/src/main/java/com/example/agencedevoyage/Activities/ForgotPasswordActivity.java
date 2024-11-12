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

import com.example.agencedevoyage.Dao.UserDao;
import com.example.agencedevoyage.Database.AppDatabase;
import com.example.agencedevoyage.Database.MailSender;
import com.example.agencedevoyage.Entity.User;
import com.example.agencedevoyage.R;
import android.util.Log;
// Import for networking and JSON handling if using an HTTP client (e.g., OkHttp, Retrofit, or Volley)
public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetPasswordButton;
    private TextView backToLogin;
    private String confirmationCode;

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

                // Generate a 4-digit OTP
                confirmationCode = String.valueOf((int) ((Math.random() * 9000) + 1000));

                // Send confirmation code
                sendConfirmationCode(email, confirmationCode);
            }
        });

        backToLogin.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void sendConfirmationCode(String email, String code) {
        AppDatabase db = AppDatabase.getInstance(this);
        UserDao userDao = db.userDao();

        new Thread(() -> {
            User user = userDao.getUserByEmail(email);
            if (user != null) {
                // Update the confirmation code in the database
                userDao.updateConfirmationCode(email, code);
                Log.d("ForgotPassword", "Confirmation code saved: " + code);

                // Send email
                MailSender mailSender = new MailSender();
                mailSender.sendConfirmationEmail(email, code);

                runOnUiThread(() -> {
                    Intent intent = new Intent(ForgotPasswordActivity.this, ConfirmCodeActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("code", code);  // This will be used to verify
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
