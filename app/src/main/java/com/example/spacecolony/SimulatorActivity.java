package com.example.spacecolony;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SimulatorActivity extends AppCompatActivity {

    private Spinner spinnerTrainees;
    private TextView tvTrainingLog;
    private ArrayList<CrewMember> crewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        spinnerTrainees = findViewById(R.id.spinnerTrainees);
        tvTrainingLog = findViewById(R.id.tvTrainingLog);
        Button btnTrain = findViewById(R.id.btnTrain);

        // 1. Initial load of the spinner when entering the screen
        refreshTraineeSpinner();

        btnTrain.setOnClickListener(v -> {
            int selectedIndex = spinnerTrainees.getSelectedItemPosition();

            // Safety check in case the list is empty
            if (selectedIndex == -1 || crewList.isEmpty()) {
                Toast.makeText(SimulatorActivity.this, "No crew members available to train!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected crew member
            CrewMember selectedCrew = crewList.get(selectedIndex);

            // 2. Energy Validation Check: Blocks exhausted crew members from training
            if (selectedCrew.getEnergy() <= 0) {
                Toast.makeText(SimulatorActivity.this, selectedCrew.getName() + " is too exhausted to train! Send them to bed.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Run the training simulation and get the result message
            String result = Simulator.train(selectedCrew);

            // Append the result to the log text view
            String currentLog = tvTrainingLog.getText().toString();
            tvTrainingLog.setText(currentLog + "\n" + result);

            // 4. Update the character state
            selectedCrew.setLocation("Quarters");
            selectedCrew.restoreFullEnergy();

            // 5. Save changes permanently to the file system
            Storage.getInstance().saveToFile(SimulatorActivity.this);

            // 6. REFRESH: Re-query storage so they disappear from the dropdown instantly
            refreshTraineeSpinner();
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // Closes the Simulator and reveals the Main dashboard
        });
    }

    // Helper method to keep the UI dropdown perfectly in sync with the data structures
    private void refreshTraineeSpinner() {
        // Fetch only the crew currently in the Simulator location
        crewList = Storage.getInstance().getCrewByLocation("Simulator");

        // Create a clean list of string names for display mapping
        ArrayList<String> crewNames = new ArrayList<>();
        for (CrewMember cm : crewList) {
            crewNames.add(cm.getName() + " (" + cm.getSpecialization() + ")");
        }

        // Re-bind the adapter with the fresh dataset
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, crewNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainees.setAdapter(adapter);
    }
}