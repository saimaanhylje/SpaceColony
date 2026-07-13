package com.example.spacecolony;

public class Simulator {

    // Trains a crew member, and returns a log message of what happened
    public static String train(CrewMember cm) {
        // Check if the crew member has enough energy to train
        if (cm.getEnergy() >= 5) {
            // apply a flat energy cost.
            cm.defend(5 + cm.resilience); // Ensures a flat 5 energy drop by bypassing resilience
            cm.gainExperience(2); // Awards 2 XP per session
            return cm.getName() + " completed training! (+2 XP, -5 Energy)";
        } else {
            return cm.getName() + " is too exhausted to train.";
        }
    }

}
