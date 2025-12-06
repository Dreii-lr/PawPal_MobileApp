package com.example.pawpal_final.ui.registration;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pawpal_final.R;
import com.google.android.material.button.MaterialButton;

public class CodeVerificationFragment extends Fragment {

    // Views matching your XML IDs
    private TextView tvEmailDisplay;
    private TextView tvResendLink;
    private TextView tvBackToSignIn;
    private MaterialButton btnOpenEmailApp;

    private String userEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Ensure this matches your XML file name
        View view = inflater.inflate(R.layout.fragment_code_verification, container, false);

        // Get email from arguments passed from Registration
        if (getArguments() != null) {
            userEmail = getArguments().getString("email", "");
        }

        initializeViews(view);
        setupClickListeners();
        displayEmail();

        return view;
    }

    private void initializeViews(View view) {
        // Updated to match the IDs in your provided XML
        tvEmailDisplay = view.findViewById(R.id.tvEmailDisplay);
        tvResendLink = view.findViewById(R.id.tvResendLink);
        btnOpenEmailApp = view.findViewById(R.id.btnOpenEmailApp);
        tvBackToSignIn = view.findViewById(R.id.tvBackToSignIn);
    }

    private void displayEmail() {
        if (userEmail != null && !userEmail.isEmpty()) {
            tvEmailDisplay.setText(userEmail);
        }
    }

    private void setupClickListeners() {
        // 1. Open Email App Button
        btnOpenEmailApp.setOnClickListener(v -> openEmailApp());

        // 2. Back to Sign In
        tvBackToSignIn.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        // 3. Resend Link (Acting as "Verify" for Demo purposes)
        tvResendLink.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Link Resent! (Proceeding to Password for Demo)", Toast.LENGTH_SHORT).show();

            // FOR DEMO ONLY: Proceed to PasswordFragment since we can't click a real email link in the simulator
            navigateToPasswordFragment();
        });
    }

    private void openEmailApp() {
        try {
            Intent intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No email app found on device", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToPasswordFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("email", userEmail);

        PasswordFragment passwordFragment = new PasswordFragment();
        passwordFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        // Ensure R.id.fragment_container matches your Activity's layout ID
        transaction.replace(R.id.fragment_container, passwordFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}