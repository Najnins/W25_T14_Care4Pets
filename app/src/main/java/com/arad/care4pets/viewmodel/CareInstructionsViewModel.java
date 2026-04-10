package com.arad.care4pets.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.arad.care4pets.utils.UserSessionManager;
import com.arad.care4pets.data.model.CareInstruction;
import com.arad.care4pets.data.repository.AppRepository;

import java.util.List;

public class CareInstructionsViewModel extends AndroidViewModel {

    private final AppRepository repository;
    private final int userId;
    private final LiveData<List<CareInstruction>> instructionsForUser;

    public CareInstructionsViewModel(Application application) {
        super(application);
        repository          = new AppRepository(application);
        userId              = UserSessionManager.getUserId(application);
        instructionsForUser = repository.getInstructionsForUser(userId);
    }

    public LiveData<List<CareInstruction>> getInstructions() {
        return instructionsForUser;
    }

    public void insert(CareInstruction instruction) {
        instruction.setUserId(userId);
        repository.insert(instruction);
    }

    public void update(CareInstruction instruction) {
        repository.update(instruction);
    }

    public void delete(CareInstruction instruction) {
        repository.delete(instruction);
    }
}