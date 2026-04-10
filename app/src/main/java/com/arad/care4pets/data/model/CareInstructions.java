package com.arad.care4pets.data.model;

import java.util.ArrayList;
import java.util.List;

public class CareInstructions {
    int id;
    List<String> careInstructions;

    public CareInstructions(int id){
        this.id = id;
        this.careInstructions = new ArrayList<>();
    }
    public CareInstructions(){
        this.careInstructions = new ArrayList<>();
    }

    public int getId(){
        return id;
    }

    public List<String> getCareInstructions(){
        return careInstructions;
    }

    public void addCareInstructions(String instructions){
        careInstructions.add(instructions);
    }

    public void removeInctructions(String instructions){
        careInstructions.remove(instructions);
    }
}
