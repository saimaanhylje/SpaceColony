package com.example.spacecolony;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView otsikko;
    private Button button1, button2, button3, button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        otsikko = findViewById(R.id.otsikko);

        //crew data pulled from file:
        Storage.getInstance().loadFromFile(this);
    }

    public void switchToRecruit(View view) {
        // Create an Intent to transition to RecruitActivity
        Intent intent = new Intent(this, RecruitActivity.class);
        startActivity(intent);
    }

    public void switchToQuarters(View view) {
        // Create an Intent to transition to QuartersActivity
        Intent intent = new Intent(this, QuartersActivity.class);
        startActivity(intent);
    }

    public void switchToSimulator(View view) {
        // Create an Intent to transition to SimulatorActivity
        Intent intent = new Intent(this, SimulatorActivity.class);
        startActivity(intent);
    }

    public void switchToMissionControl(View view) {
        // Create an Intent to transition to MissionActivity
        Intent intent = new Intent(this, MissionActivity.class);
        startActivity(intent);
    }

    public void resetApp(View view) {
        // 1. Delete the physical save file from the device
        boolean deleted = deleteFile("colony_data.ser");

        if (deleted) {
            // 2. Clear out the active runtime memory map inside Storage
            Storage.getInstance().getAllCrew().clear();

            // 3. Force the storage singleton to completely recreate a fresh empty map
            //call loadFromFile because its exception block resets it to a blank HashMap
            Storage.getInstance().loadFromFile(this);

            Toast.makeText(this, "Colony data wiped successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existing data to wipe.", Toast.LENGTH_SHORT).show();
        }
    }


}