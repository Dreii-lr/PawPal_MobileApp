package com.example.pawpal_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.pawpal_final.data.model.Schedule; // Make sure to import your model
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import java.util.Locale;

public class CreateScheduleBottomSheet extends BottomSheetDialogFragment {

    private TimePicker timePicker;
    private RadioGroup rgDispenseType;
    private RadioButton rbFood, rbWater;
    private TextView tvScheduleDiff;
    private MaterialButton btnSave;
    private OnScheduleActionListener listener;

    // Variables for Edit Mode
    private boolean isEditMode = false;
    private int editPosition = -1;
    private String existingTime;
    private String existingType;

    // Updated Interface to include position
    public interface OnScheduleActionListener {
        void onScheduleAction(String time, String type, int position);
    }

    public void setOnScheduleActionListener(OnScheduleActionListener listener) {
        this.listener = listener;
    }

    // New method to set data for editing
    public void setScheduleData(Schedule schedule, int position) {
        this.isEditMode = true;
        this.editPosition = position;
        this.existingTime = schedule.getTime();
        this.existingType = schedule.getType();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        // Populate views if in Edit Mode
        if (isEditMode) {
            populateExistingData();
        }

        setupListeners();
    }

    private void initViews(View view) {
        timePicker = view.findViewById(R.id.timePicker);
        rgDispenseType = view.findViewById(R.id.rgDispenseType);
        rbFood = view.findViewById(R.id.rbFood);
        rbWater = view.findViewById(R.id.rbWater);
        tvScheduleDiff = view.findViewById(R.id.tvScheduleDiff);
        btnSave = view.findViewById(R.id.btnCancel); // ID was btnCancel in XML

        // Set 24-hour format logic for the picker widget
        timePicker.setIs24HourView(false);

        if(isEditMode) {
            btnSave.setText("Update Schedule");
            ((TextView)view.findViewById(R.id.tvScheduleDiff)).setText("Edit Schedule"); // Optionally change title
        }
    }

    private void populateExistingData() {
        // 1. Set Radio Button
        if ("Water".equalsIgnoreCase(existingType)) {
            rbWater.setChecked(true);
        } else {
            rbFood.setChecked(true);
        }

        // 2. Parse Time String (e.g., "08:30 PM") back to integers
        // Note: This assumes strict format "hh:mm aa"
        try {
            String[] parts = existingTime.split(" "); // ["08:30", "PM"]
            String[] timeParts = parts[0].split(":"); // ["08", "30"]

            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);
            String amPm = parts[1];

            // Convert 12h to 24h for the TimePicker setters
            if (amPm.equalsIgnoreCase("PM") && hour != 12) {
                hour += 12;
            } else if (amPm.equalsIgnoreCase("AM") && hour == 12) {
                hour = 0;
            }

            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            updateScheduleDifference(hourOfDay, minute);
        });

        btnSave.setOnClickListener(v -> saveSchedule());

        updateScheduleDifference(timePicker.getHour(), timePicker.getMinute());
    }

    private void updateScheduleDifference(int hour, int minute) {
        java.util.Calendar now = java.util.Calendar.getInstance();
        java.util.Calendar selected = java.util.Calendar.getInstance();
        selected.set(java.util.Calendar.HOUR_OF_DAY, hour);
        selected.set(java.util.Calendar.MINUTE, minute);

        if (selected.before(now)) {
            selected.add(java.util.Calendar.DAY_OF_YEAR, 1);
        }

        long diffInMillis = selected.getTimeInMillis() - now.getTimeInMillis();
        long hours = diffInMillis / (60 * 60 * 1000);
        long minutes = (diffInMillis % (60 * 60 * 1000)) / (60 * 1000);

        tvScheduleDiff.setText(String.format(Locale.getDefault(),
                "Schedule in %d hours %d minutes", hours, minutes));
    }

    private void saveSchedule() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        String amPm = hour >= 12 ? "PM" : "AM";
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;

        String time = String.format(Locale.getDefault(), "%02d:%02d %s",
                displayHour, minute, amPm);

        String type = rbFood.isChecked() ? "Food" : "Water";

        if (listener != null) {
            // Pass editPosition. If it's -1, activity knows it's new.
            // If it's > -1, activity knows it's an update.
            listener.onScheduleAction(time, type, editPosition);
        }

        dismiss();
    }
}
