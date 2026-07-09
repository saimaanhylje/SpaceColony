package com.example.spacecolony;

import java.io.Serializable;

public abstract class CrewMember implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 0; // Static counter to guarantee unique IDs

    private int id;
    private String name;
    private String specialization;
    protected int skill;
    protected int resilience;
    protected int energy;
    protected int maxEnergy;
    protected int experience; // Tracked experience points

    protected String location = "Quarters";

    public CrewMember(String name, String specialization, int skill, int resilience, int maxEnergy) {
        idCounter++;
        this.id = idCounter;
        this.name = name;
        this.specialization = specialization;
        this.skill = skill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;  // Energy starts fully charged
        this.experience = 0;      // Experience starts at zero
    }

    // Returns the combined power including experience bonus
    public int act() {
        return this.skill + this.experience;
    }

    // Handles incoming damage modified by personal resilience
    public void defend(int damage) {
        int effectiveDamage = damage - this.resilience;
        if (effectiveDamage < 0) {
            effectiveDamage = 0;
        }
        this.energy -= effectiveDamage;
        if (this.energy < 0) {
            this.energy = 0;
        }
    }

    public void restoreFullEnergy() {
        this.energy = this.maxEnergy; // Retains accumulated experience points
    }

    public void gainExperience(int points) {
        this.experience += points;
    }

    // Getters and Setters for encapsulation
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getExperience() {
        return experience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
