package com.arad.care4pets.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.arad.care4pets.data.dao.CareInstructionsDao;
import com.arad.care4pets.data.dao.HealthRecordDao;
import com.arad.care4pets.data.dao.PetDao;
import com.arad.care4pets.data.dao.ReminderDao;
import com.arad.care4pets.data.dao.UserDao;
import com.arad.care4pets.data.model.CareInstruction;
import com.arad.care4pets.data.model.HealthRecord;
import com.arad.care4pets.data.model.Pet;
import com.arad.care4pets.data.model.Reminder;
import com.arad.care4pets.data.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Pet.class, Reminder.class, HealthRecord.class, CareInstruction.class, User.class},
        version = 7,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PetDao petDao();
    public abstract ReminderDao reminderDao();
    public abstract HealthRecordDao healthRecordDao();
    public abstract CareInstructionsDao careInstructionsDao();
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    // ── Migration 2 → 3 ──────────────────────────────────────────────────────
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE reminders ADD COLUMN petId INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE reminders ADD COLUMN notificationId INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE health_records ADD COLUMN petId INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE health_records ADD COLUMN dateRecorded TEXT");
        }
    };

    // ── Migration 3 → 4 ──────────────────────────────────────────────────────
    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE care_instructions ADD COLUMN petId INTEGER NOT NULL DEFAULT 0");
        }
    };

    // ── Migration 4 → 5 ──────────────────────────────────────────────────────
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "email TEXT, " +
                            "passwordHash TEXT, " +
                            "name TEXT" +
                            ")"
            );
            db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_users_email ON users(email)"
            );
        }
    };

    // ── Migration 5 → 6 ──────────────────────────────────────────────────────
    // Adds userId to pets and reminders so each user sees only their own data.
    // Existing rows default to userId = 0 (no owner — they were seed/test data).
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE pets ADD COLUMN userId INTEGER NOT NULL DEFAULT 0");
            db.execSQL("ALTER TABLE reminders ADD COLUMN userId INTEGER NOT NULL DEFAULT 0");
        }
    };

    // Migration 6 - 7
    static final Migration MIGRATION_6_7 = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL("ALTER TABLE care_instructions ADD COLUMN userId INTEGER NOT NULL DEFAULT 0");
        }
    };

    // ── Seed callback ─────────────────────────────────────────────────────────
    // Seed data removed — we have no userId to attach to seed records at
    // database creation time (no user is logged in yet).
    // Each user starts with an empty pet list and empty reminder list.
    private static final RoomDatabase.Callback seedCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            // intentionally empty
        }
    };

    // ── Singleton ─────────────────────────────────────────────────────────────
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "care4pets_database"
                            )
                            .addMigrations(
                                    MIGRATION_2_3,
                                    MIGRATION_3_4,
                                    MIGRATION_4_5,
                                    MIGRATION_5_6,
                                    MIGRATION_6_7
                            )
                            .addCallback(seedCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}