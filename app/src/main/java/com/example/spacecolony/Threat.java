package com.example.spacecolony;

import java.util.Random;

public class Threat {
    private String name;
    private int skill;
    private int resilience;
    private int energy;
    private int maxEnergy;

    // List of possible threats
    private static final String[] THREATS = {
            "Asteroids!!!",
            "Aliens try to eat you",
            "The fuel tank's leaking.",
            "Solar Flare Surge",
            "Rogue AI Overdrive"
    };

    // Constructor only needs the number of completed missions
    public Threat(int completedMissions) {
        Random rand = new Random();
        this.name = THREATS[rand.nextInt(THREATS.length)]; // Randomly selects the threat

        // scaling formula:
        // Skill grows linearly. Resilience bumps up every 3 missions. Energy scales heavily.
        this.skill = 4 + completedMissions;
        this.resilience = 2 + (completedMissions / 3);
        this.maxEnergy = 25 + (completedMissions * 5);
        this.energy = this.maxEnergy;
    }

    // Returns the threat's attack power
    public int act() {
        return this.skill;
    }

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

    public boolean isDefeated() {
        return this.energy <= 0;
    }

    // Getters for UI reporting
    public String getName() { return name; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
}
