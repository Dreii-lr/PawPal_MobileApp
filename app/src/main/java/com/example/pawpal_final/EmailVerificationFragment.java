package com.example.pawpal_final;

import static android.content.Intent.getIntent;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pawpal_final.ui.SigninActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EmailVerificationFragment extends Fragment {

    private TextInputEditText emailInput;
    private MaterialButton sendCodeButton;
    private TextView backToSignIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Just inflate layout here. No logic after return statement.
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views
        emailInput = view.findViewById(R.id.emailInput);
        sendCodeButton = view.findViewById(R.id.sendCodeButton);
        backToSignIn = view.findViewById(R.id.backToSignIn);

        // 2. Button: Send Code (Navigate to Code Verification)
        sendCodeButton.setOnClickListener(v -> handleSendCode());

        // 3. Button: Back to Sign In
        backToSignIn.setOnClickListener(v -> navigateToSignIn());
    }

    // Note: handleIntentNavigation removed. This belongs in MainActivity.

    private void handleSendCode() {
        String email = emailInput.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            return;
        }

        // Logic to simulate sending email
        Toast.makeText(requireContext(), "Code sent to " + email, Toast.LENGTH_SHORT).show();

        // Navigate to CodeVerificationFragment
        navigateToCodeVerification(email);
    }

    private void navigateToCodeVerification(String email) {
        // Create the next fragment
        CodeVerificationFragment nextFragment = new CodeVerificationFragment();

        // Pass the email to the next fragment so it can be displayed
        Bundle args = new Bundle();
        args.putString("email", email);
        nextFragment.setArguments(args);

        // Perform the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // IMPORTANT: Ensure 'fragment_container' matches the ID in your MainActivity layout
        transaction.replace(R.id.fragment_container, nextFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToSignIn() {
        Intent intent = new Intent(requireContext(), SigninActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}