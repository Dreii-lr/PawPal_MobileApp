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

public class UsernameFragment extends Fragment {

    private TextInputEditText usernameInput;
    private MaterialButton nextButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_username, container, false);

        usernameInput = view.findViewById(R.id.usernameInput);
        nextButton = view.findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter a username", Toast.LENGTH_SHORT).show();
            } else if (username.length() < 3) {
                Toast.makeText(getActivity(), "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
            } else {
                // Navigate to password fragment
                ((MainActivity) getActivity()).loadFragment(new PasswordFragment());
            }
        });

        view.findViewById(R.id.backToSignIn).setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}