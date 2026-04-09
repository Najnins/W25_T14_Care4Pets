package com.arad.care4pets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.concurrent.Executors;

public class BootReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        if(!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) return;

        Executors.newSingleThreadExecutor().execute(() ->{
            List<Reminder> reminders = AppDatabase
                    .getDatabase(context)
                    .reminderDao()
                    .getAllRemindersSync();
            for(Reminder reminder : reminders){
                ReminderScheduler.schedule(context, reminder);
            }
        });
    }
}
