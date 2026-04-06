package com.arad.care4pets;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PetViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final LiveData<List<Pet>> allPets;

    public PetViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        allPets = repository.getAllPets();
        // populateInitialData() removed — seed data is now in AppDatabase.seedCallback
        // and only runs once, on the very first time the database is created.
    }

    public LiveData<List<Pet>> getAllPets() { return allPets; }
    public void insert(Pet pet) { repository.insert(pet); }
    public void update(Pet pet) { repository.update(pet); }
    public void delete(Pet pet) { repository.delete(pet); }
}
