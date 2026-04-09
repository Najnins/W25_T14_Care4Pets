package com.arad.care4pets;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.ConcurrentModificationException;

public class ReminderNotificationReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "care4pets_reminders";
    public static final String EXTRA_TITLE = "reminder_title";
    public static final String EXTRA_NOTES = "reminder_notes";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(Context context, Intent intent){
        String title = intent.getStringExtra(EXTRA_TITLE);
        String notes = intent.getStringExtra(EXTRA_NOTES);
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0);

        createNotificationChannel(context);
        showNotification(context,title, notes, notificationId);
    }

    private void createNotificationChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Pet Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminder for your pet's health and care");
            channel.enableVibration(true);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Context context, String title, String notes, int notificationId){
        Intent openIntent = new Intent(context, RemindersActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, notificationId, openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle(title != null ? title : "Pet Reminder")
                .setContentText(notes != null && !notes.isEmpty() ? notes : "Time to take care of your pet!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 400, 200, 400});

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) manager.notify(notificationId, builder.build());
    }
}
