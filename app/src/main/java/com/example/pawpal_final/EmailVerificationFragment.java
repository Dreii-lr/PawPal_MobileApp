package com.example.pawpal_final;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EmailVerificationFragment extends Fragment {

    private TextInputEditText emailInput;
    private MaterialButton sendCodeButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email_verification, container, false);

        emailInput = view.findViewById(R.id.emailInput);
        sendCodeButton = view.findViewById(R.id.sendCodeButton);

        sendCodeButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getActivity(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to code verification fragment
                CodeVerificationFragment fragment = CodeVerificationFragment.newInstance(email);
                ((MainActivity) getActivity()).loadFragment(fragment);
            }
        });

        view.findViewById(R.id.backToSignIn).setOnClickListener(v -> {
            // Handle back to sign in
            Toast.makeText(getActivity(), "Back to Sign In", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
