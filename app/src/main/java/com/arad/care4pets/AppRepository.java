package com.arad.care4pets;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppRepository {

    private final PetDao petDao;
    private final ReminderDao reminderDao;
    private final HealthRecordDao healthRecordDao;
    private final CareInstructionsDao careInstructionsDao;
    private final LiveData<List<Pet>> allPets;
    private final LiveData<List<Reminder>> allReminders;
    private final LiveData<List<HealthRecord>> allHealthRecords;
    private final LiveData<List<CareInstruction>> allCareInstructions;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        petDao = db.petDao();
        reminderDao = db.reminderDao();
        healthRecordDao = db.healthRecordDao();
        careInstructionsDao = db.careInstructionsDao();
        allPets = petDao.getAllPets();
        allReminders = reminderDao.getAllReminders();
        allHealthRecords = healthRecordDao.getAllHealthRecords();
        allCareInstructions = careInstructionsDao.getAllInstructions();
    }

    // Pet methods
    public LiveData<List<Pet>> getAllPets() { return allPets; }
    public void insert(Pet pet) { AppDatabase.databaseWriteExecutor.execute(() -> petDao.insert(pet)); }
    public void update(Pet pet) { AppDatabase.databaseWriteExecutor.execute(() -> petDao.update(pet)); }
    public void delete(Pet pet) { AppDatabase.databaseWriteExecutor.execute(() -> petDao.delete(pet)); }

    // Reminder methods
    public LiveData<List<Reminder>> getAllReminders() { return allReminders; }

    public LiveData<List<Reminder>> getRemindersForPet(int petId) {
        return reminderDao.getRemindersForPet(petId);
    }

    /**
     * Inserts a reminder and returns its auto-generated ID synchronously.
     * MUST be called from a background thread — future.get() blocks execution.
     * The ID is needed immediately to schedule the notification alarm.
     */
    public long insertReminderAndGetId(Reminder reminder) {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(
                () -> reminderDao.insert(reminder)
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
    }

    public void update(Reminder reminder) { AppDatabase.databaseWriteExecutor.execute(() -> reminderDao.update(reminder)); }
    public void delete(Reminder reminder) { AppDatabase.databaseWriteExecutor.execute(() -> reminderDao.delete(reminder)); }

    // HealthRecord methods
    public LiveData<List<HealthRecord>> getAllHealthRecords() { return allHealthRecords; }

    public LiveData<List<HealthRecord>> getHealthRecordsForPet(int petId) {
        return healthRecordDao.getHealthRecordsForPet(petId);
    }

    public LiveData<List<HealthRecord>> getHealthRecordsByCategory(String category) {
        return healthRecordDao.getHealthRecordsByCategory(category);
    }

    public void insert(HealthRecord h) { AppDatabase.databaseWriteExecutor.execute(() -> healthRecordDao.insert(h)); }
    public void update(HealthRecord h) { AppDatabase.databaseWriteExecutor.execute(() -> healthRecordDao.update(h)); }
    public void delete(HealthRecord h) { AppDatabase.databaseWriteExecutor.execute(() -> healthRecordDao.delete(h)); }

    // CareInstruction methods
    public LiveData<List<CareInstruction>> getAllCareInstructions() { return allCareInstructions; }
    public void insert(CareInstruction i) { AppDatabase.databaseWriteExecutor.execute(() -> careInstructionsDao.insert(i)); }

}
