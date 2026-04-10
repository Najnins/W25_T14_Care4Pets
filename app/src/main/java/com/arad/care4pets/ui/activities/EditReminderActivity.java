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

    private TextInputEditText etTitle;
    private TextInputEditText etDate;
    private TextInputEditText etTime;
    private TextInputEditText etNotes;
    private SwitchMaterial switchRepeat;
    private MaterialButtonToggleGroup toggleButtonPriority;

    private final Calendar calendar = Calendar.getInstance();
    private Reminder reminderToEdit;
    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reminder);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        etTitle = findViewById(R.id.etReminderTitle);
        etDate = findViewById(R.id.etReminderDate);
        etTime = findViewById(R.id.etReminderTime);
        etNotes = findViewById(R.id.etNotes);
        switchRepeat = findViewById(R.id.switchRepeat);
        toggleButtonPriority = findViewById(R.id.toggleButtonPriority);
        Button btnSave = findViewById(R.id.btnSaveReminder);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        etDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());

        btnSave.setOnClickListener(v -> saveReminder());

        reminderToEdit = getIntent().getParcelableExtra("reminder_to_edit");
        if (reminderToEdit != null) {
            populateFields();
        }
    }

    private void populateFields() {
        etTitle.setText(reminderToEdit.getTitle());
        etDate.setText(reminderToEdit.getDate());
        etTime.setText(reminderToEdit.getTime());
        etNotes.setText(reminderToEdit.getNotes());
        switchRepeat.setChecked(reminderToEdit.isRepeating());

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

    private void showDatePicker() {
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateInView();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePicker() {
        new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    updateTimeInView();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        ).show();
    }

    private void updateDateInView() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(calendar.getTime()));
    }

    private void updateTimeInView() {
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etTime.setText(sdf.format(calendar.getTime()));
    }

    private void saveReminder() {
        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String date = etDate.getText() != null ? etDate.getText().toString().trim() : "";
        String time = etTime.getText() != null ? etTime.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int priorityButtonId = toggleButtonPriority.getCheckedButtonId();
        String priority = "Low";
        if (priorityButtonId == R.id.btnMedium) {
            priority = "Medium";
        } else if (priorityButtonId == R.id.btnHigh) {
            priority = "High";
        }

        boolean isRepeating = switchRepeat.isChecked();

        reminderToEdit.setTitle(title);
        reminderToEdit.setDate(date);
        reminderToEdit.setTime(time);
        reminderToEdit.setNotes(notes);
        reminderToEdit.setType(priority);
        reminderToEdit.setRepeating(isRepeating);

        reminderViewModel.update(reminderToEdit);

        Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}
