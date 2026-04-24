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

    // Tag for logging errors
    private static final String TAG = "DashboardActivity";

    // Calendar UI component
    private MaterialCalendarView calendarView;

    // Adapter for upcoming reminders list
    private DashboardReminderAdapter adapter;

    // ViewModel for fetching reminders
    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set dashboard layout
        setContentView(R.layout.activity_dashboard);

        // Initialize UI components
        calendarView = findViewById(R.id.calendarView);
        RecyclerView rvUpcomingReminders = findViewById(R.id.rvUpcomingReminders);
        ImageButton btnBack = findViewById(R.id.btnBack);
        TextView tvSeeAll = findViewById(R.id.tvSeeAll);

        // Setup RecyclerView for upcoming reminders
        rvUpcomingReminders.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DashboardReminderAdapter(new ArrayList<>());
        rvUpcomingReminders.setAdapter(adapter);

        // Navigate back to menu
        btnBack.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, MenuActivity.class)));

        // Navigate to full reminders screen
        tvSeeAll.setOnClickListener(v ->
                startActivity(new Intent(DashboardActivity.this, RemindersActivity.class)));

        // Initialize ViewModel and observe reminders
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            // Update calendar and list when data changes
            setupCalendar(reminders);
            setupReminders(reminders);
        });
    }

    // Highlight dates on calendar that have reminders
    private void setupCalendar(List<Reminder> reminders) {
        if (reminders == null) return;

        HashSet<CalendarDay> dates = new HashSet<>();

        // Support two date formats (because consistency is optional apparently)
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yy", Locale.US);

        for (Reminder reminder : reminders) {
            if (reminder.getDate() == null) continue;

            try {
                Date date;

                // Try parsing first format, fallback to second
                try {
                    date = format1.parse(reminder.getDate());
                } catch (ParseException e) {
                    date = format2.parse(reminder.getDate());
                }

                // Convert to CalendarDay for the calendar view
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                dates.add(CalendarDay.from(cal));

            } catch (ParseException | NullPointerException e) {
                // Log parsing errors instead of crashing
                Log.e(TAG, "Could not parse date for reminder: " + reminder.getTitle(), e);
            }
        }

        // Refresh calendar decorators (highlighted dates)
        calendarView.removeDecorators();
        calendarView.addDecorator(new EventDecorator(
                Color.parseColor("#A370F7"),
                dates
        ));
    }

    // Populate RecyclerView with filtered upcoming reminders
    private void setupReminders(List<Reminder> reminders) {
        adapter.setReminders(getUpcomingReminders(reminders));
    }

    // Filter reminders to only include today and future dates
    private List<Reminder> getUpcomingReminders(List<Reminder> reminders) {
        if (reminders == null) return new ArrayList<>();

        // Normalize today's date (midnight)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        // Same dual-format parsing problem again
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yy", Locale.US);

        // Filter reminders using Java Streams
        return reminders.stream().filter(reminder -> {
            if (reminder.getDate() == null) return false;

            try {
                Date reminderDate;

                // Try both date formats
                try {
                    reminderDate = format1.parse(reminder.getDate());
                } catch (ParseException e) {
                    reminderDate = format2.parse(reminder.getDate());
                }

                // Keep reminders that are today or in the future
                return !reminderDate.before(today);

            } catch (ParseException e) {
                Log.e(TAG, "Could not parse reminder date for: " + reminder.getTitle(), e);
                return false;
            }
        }).collect(Collectors.toList());
    }
}
