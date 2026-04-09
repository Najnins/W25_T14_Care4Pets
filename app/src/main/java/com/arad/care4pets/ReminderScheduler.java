package com.arad.care4pets;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReminderScheduler {

    private static final String TAG = "ReminderScheduler";

    public static void schedule(Context context, Reminder reminder){
        long triggerAtMillis = parseTriggerTime(reminder.getDate(), reminder.getTime());

        if(triggerAtMillis <= System.currentTimeMillis()){
            Log.w(TAG, "Skipping past reminder: " + reminder.getTitle());
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager == null) return;

        PendingIntent pendingIntent = buildPendingIntent(context, reminder);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if(alarmManager.canScheduleExactAlarms()){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }else{
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        }else{
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }

        Log.d(TAG, "Alarm Schedyuled: " + reminder.getTitle() + " @ " + triggerAtMillis);
    }

    public static void cancel(Context context, Reminder reminder){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager == null) return;
        Log.d(TAG, "Alarm cancelled: " + reminder.getTitle());
    }

    private static PendingIntent buildPendingIntent(Context context, Reminder reminder){
        Intent intent = new Intent(context, ReminderNotificationReceiver.class);
        intent.putExtra(ReminderNotificationReceiver.EXTRA_TITLE, reminder.getTitle());
        intent.putExtra(ReminderNotificationReceiver.EXTRA_NOTES, reminder.getNotes());
        intent.putExtra(ReminderNotificationReceiver.EXTRA_NOTIFICATION_ID, reminder.getId());

        return  PendingIntent.getBroadcast(
                context,
                reminder.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    private static long parseTriggerTime(String date, String time){
        try{
            String dateTimeStr;
            if(time != null && time.isEmpty()){
                dateTimeStr = date + " " + time;
            }else{
                dateTimeStr = date + "9:00 AM"; //default is 9am if no time is set
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm a", Locale.CANADA);
            Date parsed = sdf.parse(dateTimeStr);
            return parsed != null ? parsed.getTime() : System.currentTimeMillis();
        }catch (ParseException e){
            Log.e(TAG, "Failed to parse date/ time" + date + " " + time, e);
            return System.currentTimeMillis();
        }
    }

}
