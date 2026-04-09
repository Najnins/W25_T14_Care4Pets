package com.arad.care4pets;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ReminderDao {
    @Insert
    long insert(Reminder reminder);

    @Update
    void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);


    @Query("SELECT * FROM reminders WHERE userId = :userId ORDER BY date ASC, time ASC")
    LiveData<List<Reminder>> getRemindersForUser(int userId);
    @Query("SELECT * FROM reminders ORDER BY date ASC, time ASC")
    LiveData<List<Reminder>> getAllReminders();

    // FOR A SPECIFIC PET'S REMINDERS
    @Query("SELECT * FROM reminders WHERE petId = :petId ORDER BY DATE ASC")
    LiveData<List<Reminder>> getRemindersForPet(int petId);

    @Query("SELECT * FROM reminders")
    List<Reminder> getAllRemindersSync();
}
