package com.arad.care4pets.data.model;


import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Represents a registered user.
 * Email is unique — you cannot register the same email twice.
 * Password is stored as a SHA-256 hash, never plain text.
 */
@Entity(
        tableName = "users",
        indices = {@Index(value = "email", unique = true)}
)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String email;
    private String passwordHash;
    private String name;

    public User(){}

    public User(String email, String passwordHash, String name){
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    // getters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }

    // setters
    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setName(String name) { this.name = name; }
}
