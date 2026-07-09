package com.example.spacecolony;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MissionActivity extends AppCompatActivity {

    private Spinner spinnerCrew1;
    private Spinner spinnerCrew2;
    private TextView tvMissionLog;
    private ArrayList<CrewMember> crewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);

        spinnerCrew1 = findViewById(R.id.spinnerCrew1);
        spinnerCrew2 = findViewById(R.id.spinnerCrew2);
        tvMissionLog = findViewById(R.id.tvMissionLog);
        Button btnLaunchMission = findViewById(R.id.btnLaunchMission);

        // Fetch our global crew list
        crewList = Storage.getInstance().getCrewByLocation("Mission Control");

        // Populate dropdowns with crew names
        ArrayList<String> crewNames = new ArrayList<>();
        for (CrewMember cm : crewList) {
            crewNames.add(cm.getName() + " (" + cm.getSpecialization() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, crewNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCrew1.setAdapter(adapter);
        spinnerCrew2.setAdapter(adapter);

        btnLaunchMission.setOnClickListener(v -> {
            int index1 = spinnerCrew1.getSelectedItemPosition();
            int index2 = spinnerCrew2.getSelectedItemPosition();

            // Safety checks
            if (crewList.size() < 2) {
                Toast.makeText(this, "You need at least 2 crew members to launch a mission!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (index1 == index2) {
                Toast.makeText(this, "You cannot send the same person twice! Select two different members.", Toast.LENGTH_SHORT).show();
                return;
            }

            CrewMember member1 = crewList.get(index1);
            CrewMember member2 = crewList.get(index2);

            // Prevent exhausted crew from launching
            if (member1.getEnergy() <= 0 || member2.getEnergy() <= 0) {
                Toast.makeText(this, "One or both selected members are too exhausted to launch!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Execute the mission and update the UI
            String outcome = MissionControl.launchMission(member1, member2);
            tvMissionLog.setText(outcome);

            //save the changes:
            Storage.getInstance().saveToFile(this);

        });

        Button back = findViewById(R.id.back);
        back.setOnClickListener(v -> { finish();
        });

    }
}