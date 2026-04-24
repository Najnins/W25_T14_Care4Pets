package com.arad.care4pets.ui.activities;

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

import com.arad.care4pets.R;
import com.arad.care4pets.notification.ReminderScheduler;
import com.arad.care4pets.utils.UserSessionManager;
import com.arad.care4pets.data.model.Reminder;
import com.arad.care4pets.data.repository.AppRepository;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddReminderActivity extends AppCompatActivity {

    // Input fields for reminder data
    private TextInputEditText etTitle, etDate, etTime, etNotes;

    // UI controls for repeat and priority selection
    private SwitchMaterial switchRepeat;
    private MaterialButtonToggleGroup toggleButtonPriority;

    // Calendar instance for date/time selection
    private final Calendar calendar = Calendar.getInstance();

    // Repository for database operations
    private AppRepository repository;

    // Current logged-in user ID
    private int userId;

    // Launcher for requesting notification permission (Android 13+)
    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (!granted) {
                    // Notify user if notifications are disabled
                    Toast.makeText(this,
                            "Notification permission denied — reminders won't show alerts",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for adding a reminder
        setContentView(R.layout.activity_add_reminder);

        // Initialize repository and get current user ID
        repository = new AppRepository(getApplication());
        userId     = UserSessionManager.getUserId(this);

        // Initialize UI elements
        etTitle  = findViewById(R.id.etReminderTitle);
        etDate   = findViewById(R.id.etReminderDate);
        etTime   = findViewById(R.id.etReminderTime);
        etNotes  = findViewById(R.id.etNotes);
        switchRepeat         = findViewById(R.id.switchRepeat);
        toggleButtonPriority = findViewById(R.id.toggleButtonPriority);
        Button btnSave       = findViewById(R.id.btnSaveReminder);

        // Back button closes activity
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Open pickers for date and time selection
        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        // Save reminder when button is clicked
        btnSave.setOnClickListener(v -> saveReminder());

        // Default priority selection
        toggleButtonPriority.check(R.id.btnLow);

        // If a date was pre-selected (passed from another screen), set it
        String selectedDate = getIntent().getStringExtra("selected_date");
        if (selectedDate != null && !selectedDate.isEmpty()) {
            etDate.setText(selectedDate);
        }

        // Request permissions if needed
        requestNotificationPermissionIfNeeded();
        checkExactAlarmPermission();
    }

    // Request notification permission for Android 13+
    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    // Check and request permission for scheduling exact alarms (Android 12+)
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
                            // Open system settings for exact alarm permission
                            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        })
                        .setNegativeButton("Not Now", null)
                        .show();
            }
        }
    }

    // Show date picker dialog
    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, day) -> {
            calendar.set(year, month, day);

            // Format selected date and display it
            etDate.setText(new SimpleDateFormat("MM/dd/yy", Locale.CANADA)
                    .format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Show time picker dialog
    private void showTimePicker() {
        new TimePickerDialog(this, (view, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            // Format selected time (12-hour format with AM/PM)
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.CANADA);
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.CANADA);
            symbols.setAmPmStrings(new String[]{"AM", "PM"});
            sdf.setDateFormatSymbols(symbols);

            etTime.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    // Validate input and save reminder
    private void saveReminder() {
        // Extract input values safely
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String date  = etDate.getText()  != null ? etDate.getText().toString().trim()  : "";
        String time  = etTime.getText()  != null ? etTime.getText().toString().trim()  : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        // Basic validation
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine selected priority
        int priorityId = toggleButtonPriority.getCheckedButtonId();
        String priority = "Low";
        if (priorityId == R.id.btnMedium) priority = "Medium";
        else if (priorityId == R.id.btnHigh) priority = "High";

        // Create reminder object
        Reminder reminder = new Reminder(title, date, time, priority,
                switchRepeat.isChecked(), notes);

        // Associate reminder with current user
        reminder.setUserId(userId);

        // Insert into database on background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            long newId = repository.insertReminderAndGetId(reminder);

            if (newId > 0) {
                // Set generated ID and schedule notification
                reminder.setId((int) newId);
                ReminderScheduler.schedule(getApplicationContext(), reminder);
            }

            // Return to UI thread for feedback
            runOnUiThread(() -> {
                Toast.makeText(this, "Reminder created", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}