package com.example.spacecolony;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RecruitActivity extends AppCompatActivity {

    private EditText etCrewName;

    private ImageView ivCrewAvatar;
    private Spinner spinnerSpecialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        etCrewName = findViewById(R.id.etCrewName);
        spinnerSpecialization = findViewById(R.id.spinnerSpecialization);

        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnCancel = findViewById(R.id.btnCancel);
        ivCrewAvatar = findViewById(R.id.ivCrewAvatar);

        // dropdown with the five specializations
        String[] roles = {"Pilot", "Engineer", "Medic", "Scientist", "Soldier"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialization.setAdapter(adapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCrewMember();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close screen and go back
            }
        });
    }

    private void createCrewMember() {
        String name = etCrewName.getText().toString().trim();
        String selectedRole = spinnerSpecialization.getSelectedItem().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
            return;
        }

        CrewMember newCrew = null;

        // Polymorphic creation matching the specialization choice
        switch (selectedRole) {
            case "Soldier":
                newCrew = new Soldier(name);
                ivCrewAvatar.setImageResource(R.drawable.sword);
                break;
            //  Pilot, Engineer, Medic, and Scientist add them here:
            case "Pilot":
                newCrew = new Pilot(name);
                ivCrewAvatar.setImageResource(R.drawable.pilot);
                break;
            case "Engineer":
                newCrew = new Engineer(name);
                ivCrewAvatar.setImageResource(R.drawable.engineer);
                break;
            case "Medic":
                newCrew = new Medic(name);
                ivCrewAvatar.setImageResource(R.drawable.medic);
                break;
            case "Scientist":
                newCrew = new Scientist(name);
                ivCrewAvatar.setImageResource(R.drawable.scientist);
                break;

        }

        // Save into our central system; newly recruited crew are tracking in Quarters
        Storage.getInstance().addCrewMember(newCrew);

        //Save the new cm to file:
        Storage.getInstance().saveToFile(this);

        // Reveal the avatar image on screen
        ivCrewAvatar.setVisibility(View.VISIBLE);

        //clear out the "Enter name" box
        etCrewName.setText("");

        Toast.makeText(this, name + " the " + selectedRole + " recruited!", Toast.LENGTH_SHORT).show();

    }

}
