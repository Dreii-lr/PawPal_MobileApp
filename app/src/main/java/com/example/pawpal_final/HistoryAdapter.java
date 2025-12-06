package com.example.pawpal_final;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.LogViewHolder> {

    private List<HistoryLogsManager.LogItem> logList;

    public HistoryAdapter(List<HistoryLogsManager.LogItem> logList) {
        this.logList = logList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This inflates your item_logs.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logs, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        HistoryLogsManager.LogItem log = logList.get(position);

        // Set Title
        holder.logTitle.setText(log.title);

        // Set Time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.logTime.setText(sdf.format(log.time));

        // --- STATUS BADGE LOGIC ---
        if ("Success".equals(log.status)) {
            holder.logStatusText.setText("Success");
            holder.logStatusText.setTextColor(Color.parseColor("#4CAF50")); // Green Text
            holder.logStatusCard.setStrokeColor(Color.parseColor("#C9FFCC")); // Green Border
            holder.logStatusCard.setCardBackgroundColor(Color.parseColor("#E8F5E9")); // Light Green BG
        } else {
            holder.logStatusText.setText("Missed");
            holder.logStatusText.setTextColor(Color.parseColor("#FF5252")); // Red Text
            holder.logStatusCard.setStrokeColor(Color.parseColor("#FFCDD2")); // Red Border
            holder.logStatusCard.setCardBackgroundColor(Color.parseColor("#FFEBEE")); // Light Red BG
        }

        // --- ICON LOGIC ---
        if ("Water".equals(log.type)) {
            // WATER: Set Blue Drop
            holder.logIcon.setImageResource(R.drawable.ic_water_drop);
            holder.logIcon.setColorFilter(Color.parseColor("#2196F3")); // Blue
        } else {
            // FOOD: Set Orange Bone (Same logic as Water)
            // We must strictly set this here to fix recycling issues
            holder.logIcon.setImageResource(R.drawable.ic_bone);
            holder.logIcon.setColorFilter(Color.parseColor("#FF9800")); // Orange
        }
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView logTitle, logTime, logStatusText;
        ImageView logIcon;
        MaterialCardView logStatusCard;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            logTitle = itemView.findViewById(R.id.log_title);
            logTime = itemView.findViewById(R.id.log_time);
            logIcon = itemView.findViewById(R.id.log_icon);

            // Note: In your item_logs.xml, the status text is inside a card
            logStatusCard = itemView.findViewById(R.id.log_status);
            // We need to get the TextView *inside* that card. 
            // Since the TextView inside doesn't have an ID in the XML you sent, 
            // we can assume it's the first child, or you can add an ID to it.
            // For safety here, I will treat log_status as the CARD, 
            // and find the TextView inside it.
            logStatusText = (TextView) logStatusCard.getChildAt(0);
        }
    }
}
