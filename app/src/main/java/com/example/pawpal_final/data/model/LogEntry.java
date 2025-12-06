package com.example.pawpal_final.data.model;

public class LogEntry {
    String id;
    String title;       // e.g., "Food Dispense"
    String time;        // e.g., "08:00 AM"
    String status;      // "Pending", "Success", "Failed"
    String type;        // "Food" or "Water"

    public LogEntry(String id, String title, String time, String status, String type) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.status = status;
        this.type = type;
    }
}
