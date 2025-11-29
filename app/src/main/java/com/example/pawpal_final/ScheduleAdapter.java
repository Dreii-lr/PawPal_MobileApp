package com.example.pawpal_final;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pawpal_final.model.Schedule;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> scheduleList;
    private OnScheduleClickListener listener;

    public interface OnScheduleClickListener {
        void onScheduleToggle(int position, boolean isEnabled);
        void onScheduleDelete(int position);
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

    public void removeSchedule(int position) {
        scheduleList.remove(position);
        notifyItemRemoved(position);
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

            // Set icon based on type
            if (schedule.getType().equals("Food")) {
                iconImage.setImageResource(R.drawable.selector_food_selected);
            } else {
                iconImage.setImageResource(R.drawable.selector_water_selected);
            }

            // Handle switch toggle
            switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                schedule.setEnabled(isChecked);
                if (listener != null) {
                    listener.onScheduleToggle(position, isChecked);
                }
            });

            // Handle long click to delete
            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onScheduleDelete(position);
                }
                return true;
            });
        }
    }
}
