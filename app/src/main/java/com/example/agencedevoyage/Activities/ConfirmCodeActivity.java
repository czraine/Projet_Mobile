package com.example.agencedevoyage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class ConfirmCodeActivity extends AppCompatActivity {

    private EditText codeInput;
    private Button confirmButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        codeInput = findViewById(R.id.codeInput);
        confirmButton = findViewById(R.id.confirmCodeButton);

        email = getIntent().getStringExtra("email");

        confirmButton.setOnClickListener(v -> {
            String enteredCode = codeInput.getText().toString().trim();
            if (TextUtils.isEmpty(enteredCode)) {
                Toast.makeText(this, "Enter the code sent to your email", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase db = AppDatabase.getInstance(this);
            UserDao userDao = db.userDao();

            new Thread(() -> {
                User user = userDao.getUserByEmail(email);
                if (user != null && enteredCode.equals(user.getConfirmationCode())) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Code verified. Please reset your password.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmCodeActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Invalid code", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }
}
