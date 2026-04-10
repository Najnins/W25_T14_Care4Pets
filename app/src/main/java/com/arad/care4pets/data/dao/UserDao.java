package com.arad.care4pets.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arad.care4pets.data.model.User;

@Dao
public interface UserDao {

    // fail silently if email already exists
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    // Find user by email for login
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
}
