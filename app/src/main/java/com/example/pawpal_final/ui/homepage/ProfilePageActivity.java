package com.example.pawpal_final.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pawpal_final.NavigationManager;
import com.example.pawpal_final.R;
import com.example.pawpal_final.ui.activities.SecuritySettingsActivity;
import com.example.pawpal_final.ui.activities.UserInformationActivity;

public class ProfilePageActivity extends AppCompatActivity {

    private LinearLayout btnSecuritySettings, btnUserInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_profile_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnUserInformation = findViewById(R.id.btnUserInformation);
        btnSecuritySettings = findViewById(R.id.btnSecuritySettings);

        btnUserInformation.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePageActivity.this, UserInformationActivity.class);
            startActivity(intent);
        });

        btnSecuritySettings.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePageActivity.this, SecuritySettingsActivity.class);
            startActivity(intent);
        });

        NavigationManager.setup(this, R.id.imgUser);
    }

}
