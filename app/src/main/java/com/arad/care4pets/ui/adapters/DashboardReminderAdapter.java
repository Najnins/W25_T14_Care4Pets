package com.arad.care4pets.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arad.care4pets.R;
import com.arad.care4pets.data.model.Reminder;

import java.util.List;

public class DashboardReminderAdapter extends RecyclerView.Adapter<DashboardReminderAdapter.ViewHolder> {

    private List<Reminder> reminders;

    public DashboardReminderAdapter(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reminder reminder = reminders.get(position);

        holder.title.setText(reminder.getTitle());

        // Show the user's notes in the subtitle, or hide it if empty
        String notes = reminder.getNotes();
        if (notes != null && !notes.isEmpty()) {
            holder.subtitle.setText(notes);
            holder.subtitle.setVisibility(View.VISIBLE);
        } else {
            holder.subtitle.setVisibility(View.GONE);
        }

        // Show date and time together, or just date if no time set
        String date = reminder.getDate() != null ? reminder.getDate() : "";
        String time = reminder.getTime() != null && !reminder.getTime().isEmpty()
                ? "  •  " + reminder.getTime()
                : "";
        holder.dateTime.setText(date + time);
    }

    @Override
    public int getItemCount() {
        return reminders != null ? reminders.size() : 0;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        TextView dateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title    = itemView.findViewById(R.id.tvReminderTitle);
            subtitle = itemView.findViewById(R.id.tvReminderSubtitle);
            dateTime = itemView.findViewById(R.id.tvReminderTime);
        }
    }
}