package com.arad.care4pets.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.arad.care4pets.utils.UserSessionManager;
import com.arad.care4pets.data.model.Reminder;
import com.arad.care4pets.data.repository.AppRepository;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final LiveData<List<Reminder>> remindersForUser;
    private final int userId;

    public ReminderViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        userId = UserSessionManager.getUserId(application);
        remindersForUser = repository.getRemindersForUser(userId);
    }

    public LiveData<List<Reminder>> getAllReminders() { return remindersForUser; }

    public void insert(Reminder reminder){
        reminder.setUserId(userId);
        repository.insert(reminder);
    }
    public void update(Reminder reminder) { repository.update(reminder); }
    public void delete(Reminder reminder) { repository.delete(reminder); } // was missing
}
