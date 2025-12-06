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
import com.example.pawpal_final.NavigationManager;
import com.example.pawpal_final.R;
import com.example.pawpal_final.ScheduleAdapter;
import com.example.pawpal_final.data.model.Schedule;

import java.util.ArrayList;
import java.util.List;

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
                // Keep toggle logic
                String status = isEnabled ? "enabled" : "disabled";
                // Optional: Update data model state here if needed
            }

            @Override
            public void onScheduleDelete(int position) {
                showDeleteConfirmation(position);
            }

            @Override
            public void onScheduleClick(int position, Schedule schedule) {
                // Open BottomSheet in Edit Mode
                showCreateScheduleBottomSheet(schedule, position);
            }
        });

        recyclerViewAlarms.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAlarms.setAdapter(scheduleAdapter);
    }

    private void setupListeners() {
        btnAdd.setOnClickListener(v -> {
            // Open BottomSheet in Create Mode (null schedule, -1 position)
            showCreateScheduleBottomSheet(null, -1);
        });
    }

    /**
     * Shows the bottom sheet.
     * @param scheduleToEdit Pass null if creating new.
     * @param position Pass -1 if creating new.
     */
    private void showCreateScheduleBottomSheet(Schedule scheduleToEdit, int position) {
        CreateScheduleBottomSheet bottomSheet = new CreateScheduleBottomSheet();

        // If editing, pass the data
        if (scheduleToEdit != null) {
            bottomSheet.setScheduleData(scheduleToEdit, position);
        }

        bottomSheet.setOnScheduleActionListener((time, type, pos) -> {
            if (pos == -1) {
                // Add New
                Schedule newSchedule = new Schedule(time, type, true);
                scheduleAdapter.addSchedule(newSchedule);
                Toast.makeText(this, "Schedule added", Toast.LENGTH_SHORT).show();
            } else {
                // Update Existing
                // Assuming Schedule constructor: Schedule(String time, String type, boolean isEnabled)
                // We keep the existing 'enabled' state
                boolean currentEnabledState = scheduleList.get(pos).isEnabled();
                Schedule updatedSchedule = new Schedule(time, type, currentEnabledState);

                scheduleAdapter.updateSchedule(pos, updatedSchedule);
                Toast.makeText(this, "Schedule updated", Toast.LENGTH_SHORT).show();
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