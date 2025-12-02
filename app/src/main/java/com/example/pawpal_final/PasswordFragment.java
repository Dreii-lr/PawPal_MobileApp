package com.example.pawpal_final;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pawpal_final.ui.SigninActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PasswordFragment extends Fragment {

    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private MaterialButton completeButton;
    private TextView backToSignIn;
    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);

        // Get email from arguments
        if (getArguments() != null) {
            userEmail = getArguments().getString("email", "");
        }

        initializeViews(view);
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);
        completeButton = view.findViewById(R.id.completeButton);
        backToSignIn = view.findViewById(R.id.backToSignIn);
    }

    private void setupClickListeners() {
        completeButton.setOnClickListener(v -> handlePasswordSetup());

        backToSignIn.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Clear back stack and return to main
                getActivity().getSupportFragmentManager().popBackStack(null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    private void handlePasswordSetup() {
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        // Validation
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

        // Save user data and complete registration
        completeRegistration(userEmail, password);
    }

    private void completeRegistration(String email, String password) {
        // In a real app, you would:
        // 1. Send data to your backend API
        // 2. Save user session/token
        // 3. Navigate to home screen

        Toast.makeText(getContext(), "Account created successfully!",
                Toast.LENGTH_SHORT).show();

        // For demo, navigate to login page
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), SigninActivity.class);
            intent.putExtra("email", email);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }
}