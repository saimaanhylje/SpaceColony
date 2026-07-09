package com.example.spacecolony;

public class Soldier extends CrewMember {

    // Default constructor implementing default values
    public Soldier(String name) {
        // Super parameters: name, specialization, skill, resilience, maxEnergy
        super(name, "Soldier", 9, 0, 16);
    }
}