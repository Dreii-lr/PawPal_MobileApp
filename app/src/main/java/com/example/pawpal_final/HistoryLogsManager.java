package com.example.pawpal_final;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryLogsManager {

    private static HistoryLogsManager instance;
    private final List<LogItem> logs = new ArrayList<>();

    private HistoryLogsManager() {}

    public static HistoryLogsManager getInstance() {
        if (instance == null) instance = new HistoryLogsManager();
        return instance;
    }

    // Add log with current time (existing method)
    public void addLog(String title, String status, String type) {
        addLog(title, status, type, new Date());
    }

    // Add log with a specific time
    public void addLog(String title, String status, String type, Date time) {
        logs.add(0, new LogItem(title, status, type, time)); // newest first
    }

    public List<LogItem> getLogs() {
        return logs;
    }

    public void clearLogs() {
        logs.clear();
    }

    public static class LogItem {
        public String title, status, type;
        public Date time;

        public LogItem(String title, String status, String type, Date time) {
            this.title = title;
            this.status = status;
            this.type = type;
            this.time = time;
        }
    }
}
