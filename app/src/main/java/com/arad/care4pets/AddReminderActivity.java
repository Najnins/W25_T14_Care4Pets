package com.arad.care4pets;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddReminderActivity extends AppCompatActivity {

    private TextInputEditText etTitle, etDate, etTime, etNotes;
    private SwitchMaterial switchRepeat;
    private MaterialButtonToggleGroup toggleButtonPriority;
    private final Calendar calendar = Calendar.getInstance();
    private AppRepository repository;
    private int userId;

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (!granted) {
                    Toast.makeText(this,
                            "Notification permission denied — reminders won't show alerts",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        repository = new AppRepository(getApplication());
        userId     = UserSessionManager.getUserId(this);

        etTitle  = findViewById(R.id.etReminderTitle);
        etDate   = findViewById(R.id.etReminderDate);
        etTime   = findViewById(R.id.etReminderTime);
        etNotes  = findViewById(R.id.etNotes);
        switchRepeat         = findViewById(R.id.switchRepeat);
        toggleButtonPriority = findViewById(R.id.toggleButtonPriority);
        Button btnSave       = findViewById(R.id.btnSaveReminder);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());
        btnSave.setOnClickListener(v -> saveReminder());
        toggleButtonPriority.check(R.id.btnLow);

        String selectedDate = getIntent().getStringExtra("selected_date");
        if (selectedDate != null && !selectedDate.isEmpty()) {
            etDate.setText(selectedDate);
        }

        requestNotificationPermissionIfNeeded();
        checkExactAlarmPermission();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                new AlertDialog.Builder(this)
                        .setTitle("Allow Exact Reminders")
                        .setMessage(
                                "To notify you at the exact time you set, Care4Pets needs the " +
                                        "'Alarms & Reminders' permission.\n\nTap 'Open Settings', find " +
                                        "Care4Pets, and turn on 'Allow setting alarms and reminders'."
                        )
                        .setPositiveButton("Open Settings", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("Not Now", null)
                        .show();
            }
        }
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);
            etDate.setText(new SimpleDateFormat("MM/dd/yy", Locale.US).format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(this, (view, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            etTime.setText(new SimpleDateFormat("hh:mm a", Locale.US).format(calendar.getTime()));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void saveReminder() {
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String date  = etDate.getText()  != null ? etDate.getText().toString().trim()  : "";
        String time  = etTime.getText()  != null ? etTime.getText().toString().trim()  : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int priorityId = toggleButtonPriority.getCheckedButtonId();
        String priority = "Low";
        if (priorityId == R.id.btnMedium) priority = "Medium";
        else if (priorityId == R.id.btnHigh) priority = "High";

        Reminder reminder = new Reminder(title, date, time, priority,
                switchRepeat.isChecked(), notes);
        reminder.setUserId(userId); // stamp with current user

        Executors.newSingleThreadExecutor().execute(() -> {
            long newId = repository.insertReminderAndGetId(reminder);
            if (newId > 0) {
                reminder.setId((int) newId);
                ReminderScheduler.schedule(getApplicationContext(), reminder);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Reminder created", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}