package com.example.pawpal_final.ui.registration;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pawpal_final.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class UsernameFragment extends Fragment {

    private TextInputEditText usernameInput;
    private MaterialButton nextButton;
    private TextView backToSignIn;
    private String userEmail = "";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_username, container, false);

        // GET EMAIL from previous fragment
        if (getArguments() != null) {
            userEmail = getArguments().getString("email", "");
        }

        initializeViews(view);
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        usernameInput = view.findViewById(R.id.usernameInput);
        nextButton = view.findViewById(R.id.nextButton);
        backToSignIn = view.findViewById(R.id.backToSignIn);
    }

    private void setupClickListeners() {

        nextButton.setOnClickListener(v -> handleNext());

        backToSignIn.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    private void handleNext() {
        String username = usernameInput.getText().toString().trim();

        // VALIDATION
        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username is required");
            usernameInput.requestFocus();
            return;
        }

        if (username.length() < 4) {
            usernameInput.setError("Username must be at least 4 characters");
            usernameInput.requestFocus();
            return;
        }

        // NAVIGATE TO PASSWORD SETUP
        navigateToPasswordFragment(username);
    }

    private void navigateToPasswordFragment(String username) {
        // Passing EMAIL + USERNAME to PasswordFragment
        Bundle bundle = new Bundle();
        bundle.putString("email", userEmail);
        bundle.putString("username", username);

        PasswordFragment passwordFragment = new PasswordFragment();
        passwordFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, passwordFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        Toast.makeText(getContext(), "Username saved!", Toast.LENGTH_SHORT).show();
    }
}