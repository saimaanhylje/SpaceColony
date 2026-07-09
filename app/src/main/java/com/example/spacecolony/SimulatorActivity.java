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

        // Fetch our global crew list
        crewList = Storage.getInstance().getCrewByLocation("Simulator");

        // Create a list of just the crew names for the dropdown
        ArrayList<String> crewNames = new ArrayList<>();
        for (CrewMember cm : crewList) {
            crewNames.add(cm.getName() + " (" + cm.getSpecialization() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, crewNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrainees.setAdapter(adapter);

        btnTrain.setOnClickListener(v -> {
            int selectedIndex = spinnerTrainees.getSelectedItemPosition();

            // Safety check in case the list is empty
            if (selectedIndex == -1 || crewList.isEmpty()) {
                Toast.makeText(this, "No crew members available to train!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the selected crew member
            CrewMember selectedCrew = crewList.get(selectedIndex);

            // Run the training simulation and get the result message
            String result = Simulator.train(selectedCrew);

            // Append the result to the log text
            String currentLog = tvTrainingLog.getText().toString();
            tvTrainingLog.setText(currentLog + "\n" + result);

            selectedCrew.setLocation("Quarters");
            selectedCrew.restoreFullEnergy();

            //save changes :
            Storage.getInstance().saveToFile(this);
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // closes the Simulator and reveals the Main dashboard
        });

    }
}
