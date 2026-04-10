package com.arad.care4pets.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.R;
import com.arad.care4pets.data.model.HealthRecord;

public class HealthRecordAdapter extends ListAdapter<HealthRecord, HealthRecordAdapter.ViewHolder> {

    public HealthRecordAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<HealthRecord> DIFF_CALLBACK = new DiffUtil.ItemCallback<HealthRecord>() {
        @Override
        public boolean areItemsTheSame(@NonNull HealthRecord oldItem, @NonNull HealthRecord newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull HealthRecord oldItem, @NonNull HealthRecord newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthRecord record = getItem(position);
        holder.tvTitle.setText(record.getTitle());
        holder.tvDescription.setText(record.getDescription());

        // In a real app, you would set the icon based on the category
        holder.ivIcon.setImageResource(R.drawable.ic_health_notes);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
        TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvTitle = itemView.findViewById(R.id.tvRecord);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
