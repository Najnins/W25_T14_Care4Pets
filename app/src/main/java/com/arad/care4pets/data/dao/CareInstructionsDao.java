package com.arad.care4pets.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arad.care4pets.data.model.CareInstruction;

import java.util.List;

@Dao
public interface CareInstructionsDao {

    @Insert
    void insert(CareInstruction instruction);

    @Update
    void update(CareInstruction instruction);

    @Delete
    void delete(CareInstruction instruction);

    @Query("SELECT * FROM care_instructions WHERE userId = :userId ORDER BY id ASC")
    LiveData<List<CareInstruction>> getInstructionsForUser(int userId);
}