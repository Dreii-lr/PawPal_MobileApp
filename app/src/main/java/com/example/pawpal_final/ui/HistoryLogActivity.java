package com.example.pawpal_final.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pawpal_final.NavigationManager;
import com.example.pawpal_final.R;
import com.example.pawpal_final.HistoryLogsManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class HistoryLogActivity extends AppCompatActivity {

    private LinearLayout logContainer;
    private TextView clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_logs);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_history_logs), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logContainer = findViewById(R.id.log_container);
        clearButton = findViewById(R.id.clear_button);

        clearButton.setOnClickListener(v -> {
            HistoryLogsManager.getInstance().clearLogs();
            logContainer.removeAllViews();
            Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
        });

        loadLogs();

        NavigationManager.setup(this, R.id.imgHistoryLogs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLogs(); // Refresh logs whenever the activity comes into view
    }

    private void loadLogs() {
        logContainer.removeAllViews(); // safety
        for (HistoryLogsManager.LogItem log : HistoryLogsManager.getInstance().getLogs()) {
            addLog(log);
        }
    }

    private void addLog(HistoryLogsManager.LogItem log) {
        View logView = LayoutInflater.from(this)
                .inflate(R.layout.item_logs, logContainer, false);

        TextView logTitle = logView.findViewById(R.id.log_title);
        TextView logTime = logView.findViewById(R.id.log_time);
        TextView logStatus = logView.findViewById(R.id.log_status);

        logTitle.setText(log.title);

        String timeStr = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(log.time);
        logTime.setText(timeStr);

        if ("Success".equals(log.status)) {
            logStatus.setText("Success");
            logStatus.setTextColor(0xFF4CAF50);
            logStatus.setBackgroundColor(0xFFE8F5E9);
        } else {
            logStatus.setText("Missed");
            logStatus.setTextColor(0xFFE53935);
            logStatus.setBackgroundColor(0xFFFFEBEE);
        }

        logContainer.addView(logView, 0); // newest on top


    }
}
