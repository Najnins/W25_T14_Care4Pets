package com.arad.care4pets.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

    private ReminderAdapter adapter;
    private TextView tvRemindersSubtitle;
    private MaterialButtonToggleGroup toggleButtonFilter;
    private ReminderViewModel reminderViewModel;
    private List<Reminder> allReminders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        RecyclerView rvReminders = findViewById(R.id.rvReminders);
        tvRemindersSubtitle = findViewById(R.id.tvRemindersSubtitle);
        toggleButtonFilter = findViewById(R.id.toggleButtonFilter);
        ExtendedFloatingActionButton fabAddReminder = findViewById(R.id.fabAddReminder);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        fabAddReminder.setOnClickListener(v ->
                startActivity(new Intent(this, AddReminderActivity.class)));

        adapter = new ReminderAdapter(new ArrayList<>(), this);
        rvReminders.setLayoutManager(new LinearLayoutManager(this));
        rvReminders.setAdapter(adapter);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        reminderViewModel.getAllReminders().observe(this, reminders -> {
            allReminders = reminders != null ? reminders : new ArrayList<>();
            filterReminders(toggleButtonFilter.getCheckedButtonId());
            updateSubtitle();
        });

        toggleButtonFilter.check(R.id.btnAll);
        toggleButtonFilter.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) filterReminders(checkedId);
        });
    }

    private void filterReminders(int checkedId) {
        adapter.setReminders(new ArrayList<>(allReminders));
    }

    private void updateSubtitle() {
        int count = allReminders.size();
        tvRemindersSubtitle.setText(
                getResources().getQuantityString(R.plurals.active_reminders, count, count));
    }

    @Override
    public void onEditClick(Reminder reminder) {
        Intent intent = new Intent(this, EditReminderActivity.class);
        intent.putExtra("reminder_to_edit", reminder);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Reminder reminder) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Reminder")
                .setMessage("Are you sure you want to delete \"" + reminder.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    ReminderScheduler.cancel(this, reminder);  // cancel the alarm
                    reminderViewModel.delete(reminder);        // remove from database
                    Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}