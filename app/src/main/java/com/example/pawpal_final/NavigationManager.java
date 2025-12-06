package com.example.pawpal_final;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;

import com.example.pawpal_final.ui.homepage.HomepageActivity;
import com.example.pawpal_final.ui.homepage.ProfilePageActivity;
import com.example.pawpal_final.ui.homepage.SchedulePageActivity;
import com.example.pawpal_final.ui.homepage.HistoryLogActivity;
public class NavigationManager {

    public static ImageView imgHome, imgScheduling, imgUser, imgHistoryLogs;

    public static void setup (final Context context, int activeId) {
        Activity activity = (Activity) context;

        imgHome = activity.findViewById(R.id.imgHome);
        imgScheduling = activity.findViewById(R.id.imgScheduling);
        imgHistoryLogs = activity.findViewById(R.id.imgHistoryLogs);
        imgUser = activity.findViewById(R.id.imgUser);

        // Animate active/inactive icons
        animateButton(imgHome, activeId == R.id.imgHome);
        animateButton(imgScheduling, activeId == R.id.imgScheduling);
        animateButton(imgHistoryLogs, activeId == R.id.imgHistoryLogs);
        animateButton(imgUser, activeId == R.id.imgUser);

        // Set navigation clicks
        setClickListener(context, imgHome, HomepageActivity.class);
        setClickListener(context, imgScheduling, SchedulePageActivity.class);
        setClickListener(context, imgHistoryLogs, HistoryLogActivity.class);
        setClickListener(context, imgUser, ProfilePageActivity.class);
    }

    public static void setClickListener(final Context context, ImageView img, final Class<?> destination) {
        img.setOnClickListener(v -> {
            // prevent reopening same page
            if (context.getClass() != destination) {
                Intent intent = new Intent(context, destination);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
            }
        });
    }

    // Animation
    public static void animateButton(final ImageView img, boolean isActive) {
        if (isActive) {
            img.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .translationY(-15f)
                    .setDuration(300)
                    .withStartAction(() -> img.setColorFilter(Color.parseColor("#5B50D6")))
                    .start();
        } else {
            img.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .translationY(0f)
                    .setDuration(300)
                    .withStartAction(() -> img.setColorFilter(Color.parseColor("#80FFFFFF")))
                    .start();
        }
    }
}
