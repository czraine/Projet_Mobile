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

import com.example.agencedevoyage.R;
public class ConfirmCodeActivity extends AppCompatActivity {

    private EditText codeInput;
    private Button confirmCodeButton;
    private String sentCode, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        // Retrieve email and sent code from Intent
        email = getIntent().getStringExtra("email");
        sentCode = getIntent().getStringExtra("code");

        codeInput = findViewById(R.id.codeInput);
        confirmCodeButton = findViewById(R.id.confirmCodeButton);

        confirmCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = codeInput.getText().toString().trim();

                if (TextUtils.isEmpty(enteredCode)) {
                    Toast.makeText(ConfirmCodeActivity.this, "Enter the code sent to your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (enteredCode.equals(sentCode)) {
                    Intent intent = new Intent(ConfirmCodeActivity.this, ResetPasswordActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ConfirmCodeActivity.this, "Invalid code. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
