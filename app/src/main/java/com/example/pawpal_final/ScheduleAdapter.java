package com.example.pawpal_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawpal_final.data.model.Schedule;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> scheduleList;
    private OnScheduleClickListener listener;

    public interface OnScheduleClickListener {
        void onScheduleToggle(int position, boolean isEnabled);
        void onScheduleDelete(int position);
        void onScheduleClick(int position, Schedule schedule); // Added this
    }

    public ScheduleAdapter(List<Schedule> scheduleList, OnScheduleClickListener listener) {
        this.scheduleList = scheduleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.bind(schedule, position);
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public void addSchedule(Schedule schedule) {
        scheduleList.add(schedule);
        notifyItemInserted(scheduleList.size() - 1);
    }

    // Added method to update existing item
    public void updateSchedule(int position, Schedule schedule) {
        scheduleList.set(position, schedule);
        notifyItemChanged(position);
    }

    public void removeSchedule(int position) {
        scheduleList.remove(position);
        notifyItemRemoved(position);
        // Notify range changed to update positions of subsequent items
        notifyItemRangeChanged(position, scheduleList.size());
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView tvTime;
        TextView tvDesc;
        SwitchMaterial switchToggle;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImage);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            switchToggle = itemView.findViewById(R.id.switchToggle);
        }

        public void bind(Schedule schedule, int position) {
            tvTime.setText(schedule.getTime());
            tvDesc.setText(schedule.getDescription());
            switchToggle.setChecked(schedule.isEnabled());

            if (schedule.getType().equals("Food")) {
                iconImage.setImageResource(R.drawable.selector_food_selected);
            } else {
                iconImage.setImageResource(R.drawable.selector_water_selected);
            }

            switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                schedule.setEnabled(isChecked);
                if (listener != null) {
                    listener.onScheduleToggle(position, isChecked);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onScheduleDelete(position);
                }
                return true;
            });

            // Added regular click listener for editing
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onScheduleClick(position, schedule);
                }
            });
        }
    }
}
