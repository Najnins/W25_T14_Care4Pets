package com.arad.care4pets.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.R;
import com.arad.care4pets.data.model.Pet;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Pet pet);
    }

    private List<Pet> pets;
    private final OnItemClickListener onItemClickListener;

    public PetAdapter(List<Pet> pets, OnItemClickListener onItemClickListener) {
        this.pets = pets;
        this.onItemClickListener = onItemClickListener;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
        notifyDataSetChanged();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvSpecies;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPetName);
            tvSpecies = itemView.findViewById(R.id.tvPetSpecies);
        }
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = pets.get(position);
        holder.tvName.setText(pet.getName());
        String speciesAge = pet.getSpecies() + ", " + pet.getAge() + " years";
        holder.tvSpecies.setText(speciesAge);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(pet));
    }

    @Override
    public int getItemCount() {
        return pets != null ? pets.size() : 0;
    }
}
