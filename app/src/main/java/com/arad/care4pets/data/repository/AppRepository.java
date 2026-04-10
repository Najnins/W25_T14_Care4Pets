package com.arad.care4pets.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.arad.care4pets.db.AppDatabase;
import com.arad.care4pets.utils.PasswordUtils;
import com.arad.care4pets.data.dao.CareInstructionsDao;
import com.arad.care4pets.data.dao.HealthRecordDao;
import com.arad.care4pets.data.dao.PetDao;
import com.arad.care4pets.data.dao.ReminderDao;
import com.arad.care4pets.data.dao.UserDao;
import com.arad.care4pets.data.model.CareInstruction;
import com.arad.care4pets.data.model.HealthRecord;
import com.arad.care4pets.data.model.Pet;
import com.arad.care4pets.data.model.Reminder;
import com.arad.care4pets.data.model.User;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppRepository {

    private final PetDao petDao;
    private final ReminderDao reminderDao;
    private final HealthRecordDao healthRecordDao;
    private final CareInstructionsDao careInstructionsDao;
    private final UserDao userDao;

    private final LiveData<List<HealthRecord>> allHealthRecords;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        petDao = db.petDao();
        reminderDao = db.reminderDao();
        healthRecordDao = db.healthRecordDao();
        careInstructionsDao = db.careInstructionsDao();
        userDao = db.userDao();
        allHealthRecords = healthRecordDao.getAllHealthRecords();
    }

    // User

    public long registerUser(String email, String plainPassword, String name) {
        String hash = PasswordUtils.hash(plainPassword);
        User user = new User(email, hash, name);
        return userDao.insert(user);
    }

    /**
     * Looks up a user by email and verifies the password.
     * Returns the User if credentials match, null otherwise.
     */
    public User login(String email, String plainPassword) {
        User user = userDao.getUserByEmail(email);
        if (user == null) return null; // email not found
        if (!PasswordUtils.verify(plainPassword, user.getPasswordHash())) return null; // wrong password
        return user;
    }

    // Pet
    public LiveData<List<Pet>> getPetsForUser(int userId){
        return petDao.getPetsForUser(userId);
    }
    public LiveData<Pet> getPetById(int petId){
        return petDao.getPetById(petId);
    }
    public void insert(Pet pet) { AppDatabase.databaseWriteExecutor.execute(() -> petDao.insert(pet)); }
    public void update(Pet pet) { AppDatabase.databaseWriteExecutor.execute(() -> petDao.update(pet)); }
    public void delete(Pet pet) { AppDatabase.databaseWriteExecutor.execute(() -> petDao.delete(pet)); }

    // Reminder

    public LiveData<List<Reminder>> getRemindersForUser(int userId){
        return  reminderDao.getRemindersForUser(userId);
    }
    public LiveData<List<Reminder>> getRemindersForPet(int petId) {
        return reminderDao.getRemindersForPet(petId);
    }

    public void insert(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() -> reminderDao.insert(reminder));
    }

    // Inserts a reminder and returns its auto-generated ID.

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

    // HealthRecord
    public LiveData<List<HealthRecord>> getAllHealthRecords() { return allHealthRecords; }

    public LiveData<List<HealthRecord>> getHealthRecordsForPet(int petId) {
        return healthRecordDao.getHealthRecordsForPet(petId);
    }

    public void insert(HealthRecord h) { AppDatabase.databaseWriteExecutor.execute(() -> healthRecordDao.insert(h)); }
    public void update(HealthRecord h) { AppDatabase.databaseWriteExecutor.execute(() -> healthRecordDao.update(h)); }
    public void delete(HealthRecord h) { AppDatabase.databaseWriteExecutor.execute(() -> healthRecordDao.delete(h)); }

    // CareInstruction
    public LiveData<List<CareInstruction>> getInstructionsForUser(int userId) {
        return careInstructionsDao.getInstructionsForUser(userId);
    }
    public void update(CareInstruction i) { AppDatabase.databaseWriteExecutor.execute(() -> careInstructionsDao.update(i));}
    public void insert(CareInstruction i) { AppDatabase.databaseWriteExecutor.execute(() -> careInstructionsDao.insert(i)); }
    public void delete(CareInstruction i) { AppDatabase.databaseWriteExecutor.execute(() -> careInstructionsDao.delete(i)); }
}