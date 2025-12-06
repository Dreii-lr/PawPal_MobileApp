package com.example.pawpal_final;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pawpal_final.ui.SigninActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class PasswordFragment extends Fragment {

    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private MaterialButton completeButton;
    private TextView backToSignIn;

    // Rules Views
    private LinearLayout passwordRulesContainer;
    private TextView ruleLength, ruleCase, ruleSymbol, ruleNumber;

    private String userEmail;

    // Colors
    private final int COLOR_RED = Color.parseColor("#EF4444");
    private final int COLOR_GREEN = Color.parseColor("#10B981");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);

        // Get email from arguments (passed from previous fragment)
        if (getArguments() != null) {
            userEmail = getArguments().getString("email", "");
        }

        initializeViews(view);
        setupClickListeners();
        setupPasswordValidation();

        return view;
    }

    private void initializeViews(View view) {
        // Updated IDs to match the new XML
        passwordInput = view.findViewById(R.id.newPasswordInput);
        confirmPasswordInput = view.findViewById(R.id.newConfirmPasswordInput);
        completeButton = view.findViewById(R.id.completeButton);
        backToSignIn = view.findViewById(R.id.backToSignIn);

        // Initialize Rules
        passwordRulesContainer = view.findViewById(R.id.passwordRulesContainer);
        ruleLength = view.findViewById(R.id.ruleLength);
        ruleCase = view.findViewById(R.id.ruleCase);
        ruleSymbol = view.findViewById(R.id.ruleSymbol);
        ruleNumber = view.findViewById(R.id.ruleNumber);
    }

    private void setupPasswordValidation() {
        // 1. Show rules when focused
        passwordInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                passwordRulesContainer.setVisibility(View.VISIBLE);
                updatePasswordStrength(passwordInput.getText().toString());
            }
            // Optional: Hide on blur, or keep visible if invalid
        });

        // 2. Validate while typing
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
        completeButton.setOnClickListener(v -> handlePasswordSetup());

        backToSignIn.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Return to Sign In Activity
                Intent intent = new Intent(getActivity(), SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void handlePasswordSetup() {
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // 1. Basic Empty Checks
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password is required");
            passwordInput.requestFocus();
            return;
        }

        // 2. Full Complexity Check
        boolean hasLength = password.length() >= 8;
        boolean hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLowercase = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasSymbol = Pattern.compile("[!@#$%^&*+=?-]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();

        if (!hasLength || !hasUppercase || !hasLowercase || !hasSymbol || !hasNumber) {
            passwordRulesContainer.setVisibility(View.VISIBLE);
            updatePasswordStrength(password);
            Toast.makeText(getContext(), "Password must meet all requirements", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Confirm Password Check
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordInput.setError("Please confirm your password");
            confirmPasswordInput.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            confirmPasswordInput.requestFocus();
            return;
        }

        // 4. Success
        completeRegistration(userEmail, password);
    }

    private void completeRegistration(String email, String password) {
        Toast.makeText(getContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();

        // Navigate to Login Page
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), SigninActivity.class);
            intent.putExtra("email", email); // Pre-fill email on login
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }
}