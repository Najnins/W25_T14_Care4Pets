package com.arad.care4pets.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arad.care4pets.data.model.HealthRecord;

import java.util.List;

@Dao
public interface HealthRecordDao {
    @Insert
    void insert(HealthRecord healthRecord);

    @Update
    void update(HealthRecord healthRecord);

    @Delete
    void delete(HealthRecord healthRecord);

    @Query("SELECT * FROM health_records ORDER BY id DESC")
    LiveData<List<HealthRecord>> getAllHealthRecords();

    // Filter by pet
    @Query("SELECT * FROM health_records WHERE petId = :petId ORDER BY id DESC")
    LiveData<List<HealthRecord>> getHealthRecordsForPet(int petId);

    // Filter by category"
    @Query("SELECT * FROM health_records WHERE category = :category ORDER BY id DESC")
    LiveData<List<HealthRecord>> getHealthRecordsByCategory(String category);
}
