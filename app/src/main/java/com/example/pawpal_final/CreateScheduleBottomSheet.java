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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import java.util.Locale;

public class CreateScheduleBottomSheet extends BottomSheetDialogFragment {

    private TimePicker timePicker;
    private RadioGroup rgDispenseType;
    private RadioButton rbFood, rbWater;
    private TextView tvScheduleDiff;
    private MaterialButton btnSave;
    private OnScheduleCreatedListener listener;

    public interface OnScheduleCreatedListener {
        void onScheduleCreated(String time, String type);
    }

    public void setOnScheduleCreatedListener(OnScheduleCreatedListener listener) {
        this.listener = listener;
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
        setupListeners();
    }

    private void initViews(View view) {
        timePicker = view.findViewById(R.id.timePicker);
        rgDispenseType = view.findViewById(R.id.rgDispenseType);
        rbFood = view.findViewById(R.id.rbFood);
        rbWater = view.findViewById(R.id.rbWater);
        tvScheduleDiff = view.findViewById(R.id.tvScheduleDiff);
        btnSave = view.findViewById(R.id.btnCancel);

        // Set 24-hour format
        timePicker.setIs24HourView(false);
    }

    private void setupListeners() {
        // Update schedule difference when time changes
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            updateScheduleDifference(hourOfDay, minute);
        });

        // Save button click
        btnSave.setOnClickListener(v -> {
            saveSchedule();
        });

        // Initial schedule difference update
        updateScheduleDifference(timePicker.getHour(), timePicker.getMinute());
    }

    private void updateScheduleDifference(int hour, int minute) {
        // Calculate time difference from now
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

        // Format time to 12-hour format with AM/PM
        String amPm = hour >= 12 ? "PM" : "AM";
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;

        String time = String.format(Locale.getDefault(), "%02d:%02d %s",
                displayHour, minute, amPm);

        String type = rbFood.isChecked() ? "Food" : "Water";

        if (listener != null) {
            listener.onScheduleCreated(time, type);
        }

        dismiss();
    }
}
