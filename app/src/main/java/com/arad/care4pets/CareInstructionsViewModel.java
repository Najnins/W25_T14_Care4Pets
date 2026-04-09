package com.arad.care4pets;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class CareInstructionsViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final int userId;
    private final LiveData<List<CareInstruction>> instructionsForUser;

    public CareInstructionsViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        userId= UserSessionManager.getUserId(application);
        instructionsForUser = repository.getInstructionsForUser(userId);
    }

    public LiveData<List<CareInstruction>> getInstructions() {
        return instructionsForUser;
    }

    public void insert(CareInstruction instruction) {
        instruction.setUserId(userId); // stamp with current user before saving
        repository.insert(instruction);
    }

    public void delete(CareInstruction instruction) {
        repository.delete(instruction);
    }
}