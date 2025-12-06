package com.example.pawpal_final.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback; // ADDED THIS IMPORT
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pawpal_final.ui.MainActivity;
import com.example.pawpal_final.R;
import com.example.pawpal_final.ui.homepage.HomepageActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private MaterialButton btnSignIn;
    private TextView createAccountText;

    private LinearLayout passwordRulesContainer;
    private TextView ruleLength, ruleCase, ruleSymbol, ruleNumber;

    private final int COLOR_RED = Color.parseColor("#EF4444");
    private final int COLOR_GREEN = Color.parseColor("#10B981");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_loginpage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();
        setupPasswordValidation();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initializeViews() {
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.newPasswordInput);
        btnSignIn = findViewById(R.id.btnSignIn);
        createAccountText = findViewById(R.id.createAccountText);

        passwordRulesContainer = findViewById(R.id.passwordRulesContainer);
        ruleLength = findViewById(R.id.ruleLength);
        ruleCase = findViewById(R.id.ruleCase);
        ruleSymbol = findViewById(R.id.ruleSymbol);
        ruleNumber = findViewById(R.id.ruleNumber);
    }

    private void setupPasswordValidation() {
        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (passwordRulesContainer != null) {
                    passwordRulesContainer.setVisibility(View.VISIBLE);
                    updatePasswordStrength(passwordInput.getText().toString());
                }
            } else {
                if (passwordRulesContainer != null) {
                    passwordRulesContainer.setVisibility(View.GONE);
                }
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePasswordStrength(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updatePasswordStrength(String password) {
        boolean isLengthValid = password.length() >= 8;
        updateRuleView(ruleLength, isLengthValid);

        boolean hasUppercase = !TextUtils.isEmpty(password) && Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLowercase = !TextUtils.isEmpty(password) && Pattern.compile("[a-z]").matcher(password).find();
        boolean isCaseValid = hasUppercase && hasLowercase;
        updateRuleView(ruleCase, isCaseValid);

        boolean hasSymbol = !TextUtils.isEmpty(password) && Pattern.compile("[!@#$%^&*+=?-]").matcher(password).find();
        updateRuleView(ruleSymbol, hasSymbol);

        boolean hasNumber = !TextUtils.isEmpty(password) && Pattern.compile("[0-9]").matcher(password).find();
        updateRuleView(ruleNumber, hasNumber);
    }

    private void updateRuleView(TextView view, boolean isValid) {
        if (view == null) return;
        if (isValid) {
            view.setTextColor(COLOR_GREEN);
        } else {
            view.setTextColor(COLOR_RED);
        }
    }

    private void setupClickListeners() {
        btnSignIn.setOnClickListener(v -> handleSignIn());

        createAccountText.setOnClickListener(v -> {
            // FIX: You cannot navigate to a Fragment class directly in an Intent.
            // You must navigate to the HOST ACTIVITY (MainActivity).
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);

            // Pass the signal to open the correct fragment
            intent.putExtra("navigate_to", "email_verification");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void handleSignIn() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (!email.toLowerCase().endsWith("@gmail.com")) {
            emailInput.setError("Only @gmail.com accounts are allowed");
            emailInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        boolean hasLength = password.length() >= 8;
        boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLowercase = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasSymbol = Pattern.compile("[!@#$%^&*+=?-]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();

        if (!hasLength || !hasUppercase || !hasLowercase || !hasSymbol || !hasNumber) {
            passwordRulesContainer.setVisibility(View.VISIBLE);
            updatePasswordStrength(password);
            Toast.makeText(this, "Please follow the password rules", Toast.LENGTH_SHORT).show();
            return;
        }

        performSignIn(email, password);
    }

    private void performSignIn(String email, String password) {
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SigninActivity.this, HomepageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}