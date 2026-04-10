package com.arad.care4pets.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.R;
import com.arad.care4pets.data.model.CareInstruction;

import java.util.ArrayList;
import java.util.List;

public class CareInstructionsAdapter extends RecyclerView.Adapter<CareInstructionsAdapter.ViewHolder> {

    public interface OnInstructionListener {
        void onEditClick(CareInstruction instruction);
        void onDeleteClick(CareInstruction instruction);
    }

    private List<CareInstruction> instructions = new ArrayList<>();
    private OnInstructionListener listener;

    public void setListener(OnInstructionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_care_instruction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CareInstruction instruction = instructions.get(position);
        holder.tvInstruction.setText(instruction.getInstruction());

        holder.btnOptions.setOnClickListener(v -> {
            if (listener == null) return;
            // Show a small popup with Edit and Delete options
            androidx.appcompat.widget.PopupMenu popup =
                    new androidx.appcompat.widget.PopupMenu(v.getContext(), v);
            popup.getMenu().add(0, 0, 0, "Edit");
            popup.getMenu().add(0, 1, 1, "Delete");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 0) {
                    listener.onEditClick(instruction);
                } else {
                    listener.onDeleteClick(instruction);
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    public void setInstructions(List<CareInstruction> instructions) {
        this.instructions = instructions;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInstruction;
        ImageButton btnOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInstruction = itemView.findViewById(R.id.tvInstruction);
            btnOptions    = itemView.findViewById(R.id.btnOptions);
        }
    }
}