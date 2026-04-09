package com.arad.care4pets;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class HealthRecordViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final LiveData<List<HealthRecord>> allHealthRecords;

    public HealthRecordViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allHealthRecords = repository.getAllHealthRecords();
    }

    public LiveData<List<HealthRecord>> getAllHealthRecords() {
        return allHealthRecords;
    }

    // Was missing — needed by PetProfileActivity and HealthRecordsActivity
    public LiveData<List<HealthRecord>> getHealthRecordsForPet(int petId) {
        return repository.getHealthRecordsForPet(petId);
    }

    public void insert(HealthRecord h) { repository.insert(h); }
    public void update(HealthRecord h) { repository.update(h); }
    public void delete(HealthRecord h) { repository.delete(h); }
}
