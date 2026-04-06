package com.arad.care4pets;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Pet.class, Reminder.class, HealthRecord.class, CareInstruction.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PetDao petDao();
    public abstract ReminderDao reminderDao();
    public abstract HealthRecordDao healthRecordDao();
    public abstract CareInstructionsDao careInstructionsDao();

    private static volatile AppDatabase INSTANCE;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    // Adds petId + notificationId to reminders, and petId + dateRecorded
    // to health_records. SQLite doesn't support adding a foreign-key column
    // via ALTER TABLE, so petId is added as a plain integer (0 = no pet).
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE reminders ADD COLUMN petId INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE reminders ADD COLUMN notificationId INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE health_records ADD COLUMN petId INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE health_records ADD COLUMN dateRecorded TEXT");
        }
    };
    // Replaces the broken populateInitialData() in PetViewModel, which was
    // inserting duplicates on every launch.
    private static final RoomDatabase.Callback seedCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                AppDatabase database = INSTANCE;
                if (database == null) return;

                PetDao petDao = database.petDao();
                ReminderDao reminderDao = database.reminderDao();
                HealthRecordDao healthDao = database.healthRecordDao();
                CareInstructionsDao careDao = database.careInstructionsDao();

                petDao.insert(new Pet("Luna", "Dog", 3, "Allergic to chicken", 25, 98));
                petDao.insert(new Pet("Milo", "Cat", 2, "Needs eye drops", 12, 95));

                reminderDao.insert(new Reminder("Luna – Vet visit", "2024-12-15", null, "Vet", false, "Annual checkup"));
                reminderDao.insert(new Reminder("Milo – Vaccination", "2025-01-05", null, "Vaccine", false, "Rabies booster"));

                healthDao.insert(new HealthRecord("Rabies Vaccine", "Given: Nov 10, 2025\nNext: Nov 10, 2026", "Vaccinations"));
                healthDao.insert(new HealthRecord("DHPP Vaccine", "Given: Oct 15, 2025\nNext: Oct 15, 2026", "Vaccinations"));
                healthDao.insert(new HealthRecord("Heartgard Plus", "1 tablet • Monthly", "Medications"));
                healthDao.insert(new HealthRecord("Bravecto", "500mg • Every 12 weeks", "Medications"));
                healthDao.insert(new HealthRecord("Annual Checkup", "Overall health is excellent. Weight: 45 lbs", "Health Notes"));
                healthDao.insert(new HealthRecord("Skin Allergy", "Prescribed medication for skin allergies.", "Health Notes"));

                careDao.insert(new CareInstruction("• Feed twice daily"));
                careDao.insert(new CareInstruction("• Walk for 30 minutes"));
            });
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "care4pets_database"
                            )
                            .addMigrations(MIGRATION_2_3)
                            .addCallback(seedCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}