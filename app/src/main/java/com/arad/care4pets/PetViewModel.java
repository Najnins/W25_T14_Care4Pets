package com.arad.care4pets;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class PetViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final LiveData<List<Pet>> petsForUser;
    private final int userId;

    public PetViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        userId = UserSessionManager.getUserId(application);
        petsForUser = repository.getPetsForUser(userId);

    }

    public LiveData<List<Pet>> getAllPets() { return petsForUser; }
    public void insert(Pet pet){
        pet.setUserId(userId);
        repository.insert(pet);
    }
    public void update(Pet pet) { repository.update(pet); }
    public void delete(Pet pet) { repository.delete(pet); }
}
