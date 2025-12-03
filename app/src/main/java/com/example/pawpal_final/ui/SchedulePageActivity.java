package com.example.pawpal_final.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawpal_final.CreateScheduleBottomSheet;
import com.example.pawpal_final.HistoryLogsManager;
import com.example.pawpal_final.NavigationManager;
import com.example.pawpal_final.R;
import com.example.pawpal_final.ScheduleAdapter;
import com.example.pawpal_final.model.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SchedulePageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAlarms;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> scheduleList;
    private TextView btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_schedule_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationManager.setup(this, R.id.imgScheduling);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerViewAlarms = findViewById(R.id.recyclerViewAlarms);
        btnAdd = findViewById(R.id.btnAdd);
        scheduleList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        scheduleAdapter = new ScheduleAdapter(scheduleList, new ScheduleAdapter.OnScheduleClickListener() {
            @Override
            public void onScheduleToggle(int position, boolean isEnabled) {
                String status = isEnabled ? "enabled" : "disabled";
                Toast.makeText(SchedulePageActivity.this,
                        "Schedule " + status, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScheduleDelete(int position) {
                showDeleteConfirmation(position);
            }
        });

        recyclerViewAlarms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlarms.setAdapter(scheduleAdapter);
    }

    private void setupListeners() {
        btnAdd.setOnClickListener(v -> {
            showCreateScheduleBottomSheet();
        });
    }

    private void showCreateScheduleBottomSheet() {
        CreateScheduleBottomSheet bottomSheet = new CreateScheduleBottomSheet();
        bottomSheet.setOnScheduleCreatedListener((time, type) -> {
            Schedule newSchedule = new Schedule(time, type, true);
            scheduleAdapter.addSchedule(newSchedule);
            Toast.makeText(this, "Schedule added: " + time + " - " + type,
                    Toast.LENGTH_SHORT).show();
            // Convert scheduled time (String) to Date
            try {
                Date scheduledDate = new SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(time);
                HistoryLogsManager.getInstance().addLog(type + " Dispense", "Success", type, scheduledDate);
            } catch (Exception e) {
                e.printStackTrace();
                // fallback if parsing fails
                HistoryLogsManager.getInstance().addLog(type + " Dispense", "Success", type);
            }
        });
        bottomSheet.show(getSupportFragmentManager(), "CreateScheduleBottomSheet");
    }

    private void showDeleteConfirmation(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Schedule")
                .setMessage("Are you sure you want to delete this schedule?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    scheduleAdapter.removeSchedule(position);
                    Toast.makeText(this, "Schedule deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}