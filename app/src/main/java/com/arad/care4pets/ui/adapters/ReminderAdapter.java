package com.arad.care4pets.ui.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.R;
import com.arad.care4pets.data.model.Reminder;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    public interface OnReminderListener {
        void onEditClick(Reminder reminder);
        void onDeleteClick(Reminder reminder);
    }

    private List<Reminder> reminders;
    private final OnReminderListener listener;

    public ReminderAdapter(List<Reminder> reminders, OnReminderListener listener) {
        this.reminders = reminders;
        this.listener = listener;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);
        holder.bind(reminder, listener);
    }

    @Override
    public int getItemCount() {
        return reminders != null ? reminders.size() : 0;
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView ivReminderIcon;
        TextView tvReminderTitle;
        TextView tvReminderDate;
        TextView tvReminderTime;
        TextView tvPriority;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            ivReminderIcon = itemView.findViewById(R.id.ivReminderIcon);
            tvReminderTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvReminderDate = itemView.findViewById(R.id.tvReminderDate);
            tvReminderTime = itemView.findViewById(R.id.tvReminderTime);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(final Reminder reminder, final OnReminderListener listener) {
            tvReminderTitle.setText(reminder.getTitle());
            tvReminderDate.setText(reminder.getDate());

            if (reminder.getTime() != null && !reminder.getTime().isEmpty()) {
                tvReminderTime.setText(reminder.getTime());
                tvReminderTime.setVisibility(View.VISIBLE);
            } else {
                tvReminderTime.setVisibility(View.GONE);
            }

            String type = reminder.getType();
            int color = Color.parseColor("#9C27B0"); // Default Purple
            int iconRes = R.drawable.ic_bell; // Default bell icon

            if (type != null) {
                tvPriority.setText(itemView.getContext().getString(R.string.priority_text, type.toUpperCase()));
                tvPriority.setVisibility(View.VISIBLE);

                switch (type.toLowerCase()) {
                    case "high":
                        color = Color.parseColor("#FF4081");
                        tvPriority.setBackgroundResource(R.drawable.bg_priority_high);
                        break;
                    case "medium":
                        color = Color.parseColor("#FFA726");
                        tvPriority.setBackgroundResource(R.drawable.bg_priority_medium);
                        break;
                    case "low":
                        color = Color.parseColor("#66BB6A");
                        tvPriority.setBackgroundResource(R.drawable.bg_priority_low);
                        break;
                    case "vet":
                        color = Color.parseColor("#9C27B0");
                        iconRes = R.drawable.ic_vet_visit;
                        tvPriority.setVisibility(View.GONE);
                        break;
                    case "vaccine":
                        color = Color.parseColor("#00BCD4");
                        iconRes = R.drawable.ic_vaccine;
                        tvPriority.setVisibility(View.GONE);
                        break;
                    default:
                        tvPriority.setVisibility(View.GONE);
                        break;
                }
            } else {
                tvPriority.setVisibility(View.GONE);
            }

            ivReminderIcon.setImageResource(iconRes);
            cardView.setStrokeColor(color);

            Drawable background = ivReminderIcon.getBackground();
            if (background != null) {
                background.mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }

            itemView.setOnClickListener(v -> listener.onEditClick(reminder));
            btnEdit.setOnClickListener(v -> listener.onEditClick(reminder));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(reminder));
        }
    }
}
