package com.arad.care4pets;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class Reminder implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String date;
    private String time;
    private String type;
    private boolean isRepeating;
    private String notes;
    private int petId;
    private int notificationId;
    private int userId;

    public Reminder() {}

    @Ignore
    public Reminder(String title, String date, String time, String type, boolean isRepeating, String notes) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.type = type;
        this.isRepeating = isRepeating;
        this.notes = notes;
        this.petId = 0;
        this.userId = 0;
    }

    // Parcelable implementation...

    protected Reminder(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        time = in.readString();
        type = in.readString();
        isRepeating = in.readByte() != 0;
        notes = in.readString();
        petId = in.readInt();
        notificationId = in.readInt();
        userId = in.readInt();

    }

    public static final Creator<Reminder> CREATOR = new Creator<Reminder>() {
        @Override
        public Reminder createFromParcel(Parcel in) { return new Reminder(in); }
        @Override
        public Reminder[] newArray(int size) { return new Reminder[size]; }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(type);
        dest.writeByte((byte) (isRepeating ? 1 : 0));
        dest.writeString(notes);
        dest.writeInt(petId);
        dest.writeInt(notificationId);
        dest.writeInt(userId);
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getType() { return type; }
    public boolean isRepeating() { return isRepeating; }
    public String getNotes() { return notes; }
    public int getPetId() { return petId; }
    public int getNotificationId() { return notificationId; }
    public int getUserId(){return userId;}

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setType(String type) { this.type = type; }
    public void setRepeating(boolean repeating) { isRepeating = repeating; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setPetId(int petId) { this.petId = petId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }
    public void setUserId(int userId){this.userId = userId;}
}
