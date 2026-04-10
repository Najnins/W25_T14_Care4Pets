package com.arad.care4pets.data.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "care_instructions")
public class CareInstruction {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String instruction;
    private int petId;
    private int userId; // links instruction to the user who created it

    public CareInstruction() {}

    @Ignore
    public CareInstruction(String instruction) {
        this.instruction = instruction;
        this.petId = 0;
        this.userId = 0;
    }

    @Ignore
    public CareInstruction(String instruction, int petId) {
        this.instruction = instruction;
        this.petId = petId;
        this.userId = 0;
    }

    @Ignore
    public CareInstruction(String instruction, int petId, int userId) {
        this.instruction = instruction;
        this.petId = petId;
        this.userId = userId;
    }

    public int getId() { return id; }
    public String getInstruction() { return instruction; }
    public int getPetId() { return petId; }
    public int getUserId() { return userId; }

    public void setId(int id) { this.id = id; }
    public void setInstruction(String instruction) { this.instruction = instruction; }
    public void setPetId(int petId) { this.petId = petId; }
    public void setUserId(int userId) { this.userId = userId; }
}