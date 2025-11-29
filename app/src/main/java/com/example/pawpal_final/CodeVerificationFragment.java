package com.example.pawpal_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class CodeVerificationFragment extends Fragment {

    private static final String ARG_EMAIL = "email";

    public static CodeVerificationFragment newInstance(String email) {
        CodeVerificationFragment fragment = new CodeVerificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }
}
