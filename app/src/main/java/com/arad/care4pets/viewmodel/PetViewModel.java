package com.arad.care4pets.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.arad.care4pets.utils.UserSessionManager;
import com.arad.care4pets.data.model.Pet;
import com.arad.care4pets.data.repository.AppRepository;

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

    public LiveData<Pet> getPetById(int petId){
        return repository.getPetById(petId);
    }

    public void insert(Pet pet){
        pet.setUserId(userId);
        repository.insert(pet);
    }
    public void update(Pet pet) { repository.update(pet); }
    public void delete(Pet pet) { repository.delete(pet); }
}
