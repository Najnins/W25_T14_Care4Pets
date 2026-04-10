package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.ui.adapters.DashboardReminderAdapter;
import com.arad.care4pets.utils.EventDecorator;
import com.arad.care4pets.R;
import com.arad.care4pets.viewmodel.ReminderViewModel;
import com.arad.care4pets.data.model.Reminder;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private MaterialCalendarView calendarView;
    private DashboardReminderAdapter adapter;
    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        calendarView = findViewById(R.id.calendarView);
        RecyclerView rvUpcomingReminders = findViewById(R.id.rvUpcomingReminders);
        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView tvSeeAll = findViewById(R.id.tvSeeAll);

        rvUpcomingReminders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DashboardReminderAdapter(new ArrayList<>());
        rvUpcomingReminders.setAdapter(adapter);

        btnBack.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, MenuActivity.class)));
        tvSeeAll.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, RemindersActivity.class)));

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            setupCalendar(reminders);
            setupReminders(reminders);
        });
    }

    private void setupCalendar(List<Reminder> reminders) {
        if (reminders == null) return;
        HashSet<CalendarDay> dates = new HashSet<>();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yy", Locale.US);

        for (Reminder reminder : reminders) {
            if (reminder.getDate() == null) continue;
            try {
                Date date;
                try {
                    date = format1.parse(reminder.getDate());
                } catch (ParseException e) {
                    date = format2.parse(reminder.getDate());
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                dates.add(CalendarDay.from(cal));
            } catch (ParseException | NullPointerException e) {
                Log.e(TAG, "Could not parse date for reminder: " + reminder.getTitle(), e);
            }
        }
        calendarView.removeDecorators();
        calendarView.addDecorator(new EventDecorator(Color.parseColor("#A370F7"), dates));
    }

    private void setupReminders(List<Reminder> reminders) {
        adapter.setReminders(getUpcomingReminders(reminders));
    }

    private List<Reminder> getUpcomingReminders(List<Reminder> reminders) {
        if (reminders == null) return new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yy", Locale.US);

        return reminders.stream().filter(reminder -> {
            if (reminder.getDate() == null) return false;
            try {
                Date reminderDate;
                try {
                    reminderDate = format1.parse(reminder.getDate());
                } catch (ParseException e) {
                    reminderDate = format2.parse(reminder.getDate());
                }
                return !reminderDate.before(today);
            } catch (ParseException e) {
                Log.e(TAG, "Could not parse reminder date for: " + reminder.getTitle(), e);
                return false;
            }
        }).collect(Collectors.toList());
    }
}
