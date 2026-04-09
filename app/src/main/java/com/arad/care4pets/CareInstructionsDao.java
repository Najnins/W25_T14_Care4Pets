package com.arad.care4pets;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface CareInstructionsDao {

    @Insert
    void insert(CareInstruction instruction);

    @Delete
    void delete(CareInstruction instruction);

    // Only fetch instructions belonging to the logged-in user
    @Query("SELECT * FROM care_instructions WHERE userId = :userId ORDER BY id ASC")
    LiveData<List<CareInstruction>> getInstructionsForUser(int userId);
}