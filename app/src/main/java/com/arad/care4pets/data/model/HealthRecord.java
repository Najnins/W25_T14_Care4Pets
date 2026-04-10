package com.arad.care4pets.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "health_records")
public class HealthRecord {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String category;
    private int petId;
    private String dateRecorded;

    public HealthRecord() {}

    @Ignore
    public HealthRecord(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.petId = 0;
    }

    @Ignore
    public HealthRecord(String title, String description, String category, int petId, String dateRecorded){
        this.title = title;
        this.description = description;
        this.category = category;
        this.petId = petId;
        this.dateRecorded = dateRecorded;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public int getPetId() { return petId; }
    public String getDateRecorded() { return dateRecorded; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setPetId(int petId) { this.petId = petId; }
    public void setDateRecorded(String dateRecorded) { this.dateRecorded = dateRecorded; }
}
