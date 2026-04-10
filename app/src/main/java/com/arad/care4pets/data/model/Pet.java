package com.arad.care4pets.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "pets")
public class Pet implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String species;
    private int age;
    private String notes;
    private int weight;
    private int health;
    private int userId;

    public Pet() {}

    @Ignore
    public Pet(String name, String species, int age, String notes, int weight, int health) {
        this.name = name;
        this.species = species;
        this.age = age;
        this.notes = notes;
        this.weight = weight;
        this.health = health;
        this.userId = 0;
    }

    @Ignore
    public Pet(String name, String species, int age, String notes, int weight, int health, int userId){
        this.name = name;
        this.species = species;
        this.age = age;
        this.notes = notes;
        this.weight = weight;
        this.health = health;
        this.userId = userId;
    }

    // Parcelable implementation...

    protected Pet(Parcel in) {
        id = in.readInt();
        name = in.readString();
        species = in.readString();
        age = in.readInt();
        notes = in.readString();
        weight = in.readInt();
        health = in.readInt();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(species);
        dest.writeInt(age);
        dest.writeString(notes);
        dest.writeInt(weight);
        dest.writeInt(health);
        dest.writeInt(userId); // links the pet to the user that owns it
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public int getAge() { return age; }
    public String getNotes() { return notes; }
    public int getWeight() { return weight; }
    public int getHealth() { return health; }
    public int getUserId(){return userId;}

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecies(String species) { this.species = species; }
    public void setAge(int age) { this.age = age; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setHealth(int health) { this.health = health; }
    public void setUserId(int userId){this.userId = userId;}
}
