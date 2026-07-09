package com.example.spacecolony;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;

public class QuartersActivity extends AppCompatActivity {

    private RecyclerView rvCrewQuarters;
    private CrewAdapter adapter;
    private ArrayList<CrewMember> crewList;
    private HashSet<Integer> selectedCrewIds = new HashSet<>(); // Set to store the IDs of currently selected crew members

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        rvCrewQuarters = findViewById(R.id.rvCrewQuarters);
        Button btnMoveToSimulator = findViewById(R.id.btnMoveToSimulator);
        Button btnMoveToMissionControl = findViewById(R.id.btnMoveToMissionControl);
        Button button5 = findViewById(R.id.button5); // Go Back button

        // 1. Initialize the list so can clear and update it later
        crewList = new ArrayList<>();
        crewList.addAll(Storage.getInstance().getCrewByLocation("Quarters"));

        //scrollable list configuration
        rvCrewQuarters.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CrewAdapter();
        rvCrewQuarters.setAdapter(adapter);

        // --- MOVE TO SIMULATOR ---
        btnMoveToSimulator.setOnClickListener(v -> {
            if (selectedCrewIds.isEmpty()) {
                Toast.makeText(this, "Select crew members first!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int id : selectedCrewIds) {
                CrewMember cm = Storage.getInstance().getCrewMember(id);
                if (cm != null) {
                    cm.setLocation("Simulator"); // Moves them out of Quarters
                }
            }
            Toast.makeText(this, "Moved selected crew to Simulator!", Toast.LENGTH_SHORT).show();
            selectedCrewIds.clear(); // Clear selections
            refreshList(); // Update the UI
        });

        // --- MOVE TO MISSION CONTROL ---
        btnMoveToMissionControl.setOnClickListener(v -> {
            if (selectedCrewIds.size() != 2) {
                Toast.makeText(this, "You must select exactly 2 crew members for a mission!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int id : selectedCrewIds) {
                CrewMember cm = Storage.getInstance().getCrewMember(id);
                if (cm != null) {
                    cm.setLocation("Mission Control"); // Moves them out of Quarters
                }
            }
            Toast.makeText(this, "Crew ready at Mission Control!", Toast.LENGTH_SHORT).show();
            selectedCrewIds.clear(); // Clear selections
            refreshList(); // Update the UI
        });

        // --- GO BACK ---
        button5.setOnClickListener(v -> {
            finish();
        });
    }

    // A helper method to clear the old data, fetch the new data, and refresh the screen
    private void refreshList() {
        crewList.clear();
        crewList.addAll(Storage.getInstance().getCrewByLocation("Quarters"));
        adapter.notifyDataSetChanged();
    }

    // This ensures the list updates automatically if user navigates away and come back
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            refreshList();
        }
    }

    // --- Inner RecyclerView Adapter Class ---
    private class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

        @NonNull
        @Override
        public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew, parent, false);
            return new CrewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
            CrewMember cm = crewList.get(position);
            holder.tvName.setText(cm.getName());

            String statsText = "Role: " + cm.getSpecialization() +
                    " | Energy: " + cm.getEnergy() + "/" + cm.getMaxEnergy() +
                    " | XP: " + cm.getExperience();
            holder.tvStats.setText(statsText);

            // Handle checkbox state safely during recycling
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(selectedCrewIds.contains(cm.getId()));

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedCrewIds.add(cm.getId());
                } else {
                    selectedCrewIds.remove(cm.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return crewList.size();
        }

        //inner class CrewViewHolder
        class CrewViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvStats;
            CheckBox checkBox;

            public CrewViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvItemCrewName);
                tvStats = itemView.findViewById(R.id.tvItemCrewStats);
                checkBox = itemView.findViewById(R.id.cbSelectCrew);
            }
        }
    }
}