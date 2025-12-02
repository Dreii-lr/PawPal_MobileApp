package com.example.pawpal_final.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback; // ADDED THIS IMPORT
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pawpal_final.MainActivity;
import com.example.pawpal_final.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SigninActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private MaterialButton btnSignIn;
    private TextView createAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // 1. Setup Window Insets (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_loginpage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Initialize Views and Listeners
        initializeViews();
        setupClickListeners();

        // 3. Handle Back Press (The Modern Way)
        // This replaces the deprecated onBackPressed() method
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to main activity
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // 4. Pre-fill email if passed from registration
        Intent intent = getIntent();
        if (intent.hasExtra("email")) {
            String email = intent.getStringExtra("email");
            if (emailInput != null) {
                emailInput.setText(email);
            }
        }
    }

    private void initializeViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        btnSignIn = findViewById(R.id.btnSignIn);
        createAccountText = findViewById(R.id.createAccountText);
    }

    private void setupClickListeners() {
        btnSignIn.setOnClickListener(v -> handleSignIn());

        createAccountText.setOnClickListener(v -> {
            // Navigate back to main activity for account creation
            Intent intent = new Intent(SigninActivity.this, HomepageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void handleSignIn() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email");
            emailInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        // Perform sign in
        performSignIn(email, password);
    }

    private void performSignIn(String email, String password) {
        // In a real app, you would:
        // 1. Call your backend API to authenticate
        // 2. Save user session/token
        // 3. Navigate to home screen

        // For demo purposes
        Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show();

        // Simulate successful login
        Toast.makeText(this, "Login successful! Welcome back.", Toast.LENGTH_LONG).show();

        // TODO: Navigate to your home/dashboard activity here
        // Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
        // startActivity(intent);
        // finish();

        Intent intent = new Intent(SigninActivity.this, HomepageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}