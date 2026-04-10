package com.arad.care4pets.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.arad.care4pets.data.model.Pet;

import java.util.List;

@Dao
public interface PetDao {
    @Insert
    void insert(Pet pet);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);


    // Returns only pets belionging to current user
    @Query("SELECT * FROM pets WHERE userId = :userId")
    LiveData<List<Pet>> getPetsForUser(int userId);
    @Query("SELECT * FROM pets")
    LiveData<List<Pet>> getAllPets();
    @Query("SELECT * FROM pets WHERE id = :petId")
    LiveData<Pet> getPetById(int petId);
}
