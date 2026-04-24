package com.arad.care4pets.ui.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.arad.care4pets.R;
import com.arad.care4pets.viewmodel.ReminderViewModel;
import com.arad.care4pets.data.model.Reminder;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditReminderActivity extends AppCompatActivity {

    // Input fields for reminder data
    private TextInputEditText etTitle;
    private TextInputEditText etDate;
    private TextInputEditText etTime;
    private TextInputEditText etNotes;

    // UI controls for repeat and priority
    private SwitchMaterial switchRepeat;
    private MaterialButtonToggleGroup toggleButtonPriority;

    // Calendar instance used for date/time selection
    private final Calendar calendar = Calendar.getInstance();

    // Reminder being edited
    private Reminder reminderToEdit;

    // ViewModel for updating reminder in database
    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout for editing reminder
        setContentView(R.layout.activity_edit_reminder);

        // Initialize ViewModel
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        // Initialize UI elements
        etTitle = findViewById(R.id.etReminderTitle);
        etDate = findViewById(R.id.etReminderDate);
        etTime = findViewById(R.id.etReminderTime);
        etNotes = findViewById(R.id.etNotes);
        switchRepeat = findViewById(R.id.switchRepeat);
        toggleButtonPriority = findViewById(R.id.toggleButtonPriority);
        Button btnSave = findViewById(R.id.btnSaveReminder);

        // Back button closes activity
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Open date picker when date field is clicked
        etDate.setOnClickListener(v -> showDatePicker());

        // Open time picker when time field is clicked
        etTime.setOnClickListener(v -> showTimePicker());

        // Save changes when button is clicked
        btnSave.setOnClickListener(v -> saveReminder());

        // Get reminder passed from previous screen
        reminderToEdit = getIntent().getParcelableExtra("reminder_to_edit");

        // If reminder exists, populate fields with current data
        if (reminderToEdit != null) {
            populateFields();
        }
    }

    // Fill input fields with existing reminder data
    private void populateFields() {
        etTitle.setText(reminderToEdit.getTitle());
        etDate.setText(reminderToEdit.getDate());
        etTime.setText(reminderToEdit.getTime());
        etNotes.setText(reminderToEdit.getNotes());
        switchRepeat.setChecked(reminderToEdit.isRepeating());

        // Set priority toggle based on reminder type
        String priority = reminderToEdit.getType();
        if (priority != null) {
            switch (priority.toLowerCase()) {
                case "high":
                    toggleButtonPriority.check(R.id.btnHigh);
                    break;
                case "medium":
                    toggleButtonPriority.check(R.id.btnMedium);
                    break;
                default:
                    toggleButtonPriority.check(R.id.btnLow);
                    break;
            }
        }
    }

    // Show date picker dialog
    private void showDatePicker() {
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // Update calendar with selected date
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Reflect selected date in UI
                    updateDateInView();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    // Show time picker dialog
    private void showTimePicker() {
        new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    // Update calendar with selected time
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    // Reflect selected time in UI
                    updateTimeInView();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false // 12-hour format
        ).show();
    }

    // Format and display selected date
    private void updateDateInView() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(calendar.getTime()));
    }

    // Format and display selected time
    private void updateTimeInView() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etTime.setText(sdf.format(calendar.getTime()));
    }

    // Validate input and save updated reminder
    private void saveReminder() {
        // Safely extract input values
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String date = etDate.getText() != null ? etDate.getText().toString().trim() : "";
        String time = etTime.getText() != null ? etTime.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        // Basic validation (title and date required)
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine selected priority
        int priorityButtonId = toggleButtonPriority.getCheckedButtonId();
        String priority = "Low";
        if (priorityButtonId == R.id.btnMedium) {
            priority = "Medium";
        } else if (priorityButtonId == R.id.btnHigh) {
            priority = "High";
        }

        // Get repeat state
        boolean isRepeating = switchRepeat.isChecked();

        // Update reminder object
        reminderToEdit.setTitle(title);
        reminderToEdit.setDate(date);
        reminderToEdit.setTime(time);
        reminderToEdit.setNotes(notes);
        reminderToEdit.setType(priority);
        reminderToEdit.setRepeating(isRepeating);

        // Save changes to database
        reminderViewModel.update(reminderToEdit);

        // Notify user and close activity
        Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}
