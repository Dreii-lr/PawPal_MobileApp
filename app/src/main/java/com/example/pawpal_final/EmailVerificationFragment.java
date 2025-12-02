package com.example.pawpal_final;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EmailVerificationFragment extends Fragment {

    private TextInputEditText emailInput;
    private MaterialButton sendCodeButton;
    private TextView backToSignIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_verification, container, false);

        initializeViews(view);
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        emailInput = view.findViewById(R.id.emailInput);
        sendCodeButton = view.findViewById(R.id.sendCodeButton);
        backToSignIn = view.findViewById(R.id.backToSignIn);
    }

    private void setupClickListeners() {
        sendCodeButton.setOnClickListener(v -> handleSendCode());

        backToSignIn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    private void handleSendCode() {
        String email = emailInput.getText().toString().trim();

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

        // Simulate sending verification code
        sendVerificationCode(email);
    }

    private void sendVerificationCode(String email) {
        // In a real app, you would call your backend API here
        Toast.makeText(getContext(), "Verification code sent to " + email,
                Toast.LENGTH_SHORT).show();

        // Navigate to code verification fragment
        Bundle bundle = new Bundle();
        bundle.putString("email", email);

        CodeVerificationFragment codeFragment = new CodeVerificationFragment();
        codeFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, codeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
