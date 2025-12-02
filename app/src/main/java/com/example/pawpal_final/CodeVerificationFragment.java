package com.example.pawpal_final;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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

public class CodeVerificationFragment extends Fragment {

    private TextView emailDisplay, backToSignIn;
    private TextInputEditText code1, code2, code3, code4;
    private MaterialButton verifyButton;
    private String userEmail;

    private static final String DEMO_CODE = "1234";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_verification, container, false);

        // Get email from arguments
        if (getArguments() != null) {
            userEmail = getArguments().getString("email", "");
        }

        initializeViews(view);
        setupCodeInputs();
        setupClickListeners();
        displayEmail();

        return view;
    }

    private void initializeViews(View view) {
        emailDisplay = view.findViewById(R.id.emailDisplay);
        code1 = view.findViewById(R.id.code1);
        code2 = view.findViewById(R.id.code2);
        code3 = view.findViewById(R.id.code3);
        code4 = view.findViewById(R.id.code4);
        verifyButton = view.findViewById(R.id.sendCodeButton);
        backToSignIn = view.findViewById(R.id.backToSignIn);
    }

    private void displayEmail() {
        if (userEmail != null && !userEmail.isEmpty()) {
            emailDisplay.setText("Code sent to " + userEmail);
        }
    }

    private void setupCodeInputs() {
        setupCodeInput(code1, code2);
        setupCodeInput(code2, code3);
        setupCodeInput(code3, code4);
        setupCodeInput(code4, null);

        // Handle backspace
        setupBackspace(code2, code1);
        setupBackspace(code3, code2);
        setupBackspace(code4, code3);
    }

    private void setupCodeInput(TextInputEditText current, TextInputEditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && next != null) {
                    next.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupBackspace(TextInputEditText current, TextInputEditText previous) {
        current.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (current.getText().toString().isEmpty() && previous != null) {
                    previous.requestFocus();
                    previous.setText("");
                    return true;
                }
            }
            return false;
        });
    }

    private void setupClickListeners() {
        verifyButton.setOnClickListener(v -> handleVerification());

        backToSignIn.setOnClickListener(v -> {
            if (getActivity() != null) {
                // Clear back stack and return to main
                getActivity().getSupportFragmentManager().popBackStack(null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });
    }

    private void handleVerification() {
        String code = code1.getText().toString() +
                code2.getText().toString() +
                code3.getText().toString() +
                code4.getText().toString();

        if (code.length() != 4) {
            Toast.makeText(getContext(), "Please enter complete code",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Verify code (demo uses 1234)
        if (code.equals(DEMO_CODE)) {
            Toast.makeText(getContext(), "Code verified successfully!",
                    Toast.LENGTH_SHORT).show();

            // Navigate to password setup
            Bundle bundle = new Bundle();
            bundle.putString("email", userEmail);

            PasswordFragment passwordFragment = new PasswordFragment();
            passwordFragment.setArguments(bundle);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, passwordFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Toast.makeText(getContext(), "Invalid code. Try 1234 for demo",
                    Toast.LENGTH_SHORT).show();
            clearCodeInputs();
        }
    }

    private void clearCodeInputs() {
        code1.setText("");
        code2.setText("");
        code3.setText("");
        code4.setText("");
        code1.requestFocus();
    }
}
