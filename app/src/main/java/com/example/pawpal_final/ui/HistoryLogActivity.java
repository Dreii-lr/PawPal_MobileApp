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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawpal_final.HistoryAdapter;
import com.example.pawpal_final.NavigationManager;
import com.example.pawpal_final.R;
import com.example.pawpal_final.HistoryLogsManager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryLogActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLogs;
    private HistoryAdapter adapter;
    private List<HistoryLogsManager.LogItem> logList;
    private View clearButton; // Changed to View to match MaterialCardView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_logs);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_history_logs), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialize Views
        // Note: Use the ID from your XML. You had it as "recyclerViewAlarms" in the XML,
        // but it's cleaner to treat it as logs.
        recyclerViewLogs = findViewById(R.id.recyclerViewAlarms);
        clearButton = findViewById(R.id.tvClearHistory);

        // 2. Setup RecyclerView
        recyclerViewLogs.setLayoutManager(new LinearLayoutManager(this));

        // Get the list from the Singleton Manager
        logList = HistoryLogsManager.getInstance().getLogs();

        // Connect Adapter
        adapter = new HistoryAdapter(logList);
        recyclerViewLogs.setAdapter(adapter);

        // 3. Setup Clear Button
        clearButton.setOnClickListener(v -> {
            HistoryLogsManager.getInstance().clearLogs();
            adapter.notifyDataSetChanged(); // Refresh the list (now empty)
            Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
        });

        NavigationManager.setup(this, R.id.imgHistoryLogs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list when returning to this page (e.g. from Schedule page)
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
