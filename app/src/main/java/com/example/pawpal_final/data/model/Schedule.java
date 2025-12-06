package com.example.pawpal_final.data.model;

public class Schedule {
    private String time;
    private String type; // "Food" or "Water"
    private boolean isEnabled;

    public Schedule(String time, String type, boolean isEnabled) {
        this.time = time;
        this.type = type;
        this.isEnabled = isEnabled;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getDescription() {
        return type;
    }
}