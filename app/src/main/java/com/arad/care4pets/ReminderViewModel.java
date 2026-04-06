package com.arad.care4pets;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final LiveData<List<Reminder>> allReminders;

    public ReminderViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allReminders = repository.getAllReminders();
    }

    public LiveData<List<Reminder>> getAllReminders() { return allReminders; }
    public void insert(Reminder reminder) { repository.insert(reminder); }
    public void update(Reminder reminder) { repository.update(reminder); }
    public void delete(Reminder reminder) { repository.delete(reminder); } // was missing
}
