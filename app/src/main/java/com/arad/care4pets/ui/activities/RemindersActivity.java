package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.R;
import com.arad.care4pets.ui.adapters.ReminderAdapter;
import com.arad.care4pets.notification.ReminderScheduler;
import com.arad.care4pets.viewmodel.ReminderViewModel;
import com.arad.care4pets.data.model.Reminder;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RemindersActivity extends AppCompatActivity implements ReminderAdapter.OnReminderListener {

    // Adapter for displaying reminders in RecyclerView
    private ReminderAdapter adapter;

    // Subtitle text showing number of active reminders
    private TextView tvRemindersSubtitle;

    // Toggle group for filtering reminders (All, Today, Upcoming, Recurring)
    private MaterialButtonToggleGroup toggleButtonFilter;

    // ViewModel for accessing reminder data
    private ReminderViewModel reminderViewModel;

    // Holds all reminders fetched from database
    private List<Reminder> allReminders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for reminders screen
        setContentView(R.layout.activity_reminders);

        // Initialize UI components
        RecyclerView rvReminders = findViewById(R.id.rvReminders);
        tvRemindersSubtitle = findViewById(R.id.tvRemindersSubtitle);
        toggleButtonFilter = findViewById(R.id.toggleButtonFilter);
        ExtendedFloatingActionButton fabAddReminder = findViewById(R.id.fabAddReminder);

        // Back button closes the activity
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Open Add Reminder screen
        fabAddReminder.setOnClickListener(v ->
                startActivity(new Intent(this, AddReminderActivity.class)));

        // Setup RecyclerView with adapter
        adapter = new ReminderAdapter(new ArrayList<>(), this);
        rvReminders.setLayoutManager(new LinearLayoutManager(this));
        rvReminders.setAdapter(adapter);

        // Initialize ViewModel and observe reminders
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            // Update local list (avoid null issues)
            allReminders = reminders != null ? reminders : new ArrayList<>();

            // Apply current filter
            filterReminders(toggleButtonFilter.getCheckedButtonId());

            // Update subtitle count
            updateSubtitle();
        });

        // Default filter is "All"
        toggleButtonFilter.check(R.id.btnAll);

        // Listen for filter changes
        toggleButtonFilter.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) filterReminders(checkedId);
        });
    }

    // Filters reminders based on selected toggle button
    private void filterReminders(int checkedId) {
        List<Reminder> filtered = new ArrayList<>();

        // Get today's date with time reset to midnight
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        // Date format used for parsing reminder dates
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yy", Locale.CANADA);

        if (checkedId == R.id.btnAll) {
            // Show all reminders
            filtered.addAll(allReminders);

        } else if (checkedId == R.id.btnToday) {
            // Show reminders scheduled for today
            for (Reminder r : allReminders) {
                Date d = parseDate(r.getDate(), fmt);
                if (d != null && isSameDay(d, today)) filtered.add(r);
            }

        } else if (checkedId == R.id.btnUpcoming) {
            // Show reminders scheduled after today
            for (Reminder r : allReminders) {
                Date d = parseDate(r.getDate(), fmt);
                if (d != null && d.after(today)) filtered.add(r);
            }

        } else if (checkedId == R.id.btnRecurring) {
            // Show only repeating reminders
            for (Reminder r : allReminders) {
                if (r.isRepeating()) filtered.add(r);
            }

        } else {
            // Fallback (shouldn't really happen)
            filtered.addAll(allReminders);
        }

        // Update adapter with filtered list
        adapter.setReminders(filtered);
    }

    // Safely parse date string into Date object
    private Date parseDate(String s, SimpleDateFormat fmt) {
        if (s == null || s.isEmpty()) return null;
        try {
            return fmt.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    // Check if two dates fall on the same calendar day
    private boolean isSameDay(Date a, Date b) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(a);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(b);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    // Update subtitle showing number of active reminders
    private void updateSubtitle() {
        int count = allReminders.size();
        tvRemindersSubtitle.setText(
                getResources().getQuantityString(R.plurals.active_reminders, count, count));
    }

    // Handle edit button click from adapter
    @Override
    public void onEditClick(Reminder reminder) {
        Intent intent = new Intent(this, EditReminderActivity.class);
        intent.putExtra("reminder_to_edit", reminder);
        startActivity(intent);
    }

    // Handle delete button click from adapter
    @Override
    public void onDeleteClick(Reminder reminder) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Reminder")
                .setMessage("Are you sure you want to delete \"" + reminder.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Cancel scheduled notification/alarm
                    ReminderScheduler.cancel(this, reminder);

                    // Remove reminder from database
                    reminderViewModel.delete(reminder);

                    // Notify user
                    Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}