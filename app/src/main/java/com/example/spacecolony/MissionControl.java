package com.example.spacecolony;

public class MissionControl {

    // Tracks successful missions to scale the difficulty
    private static int completedMissions = 0;

    // Launches a mission and returns a detailed log of the events
    public static String launchMission(CrewMember cm1, CrewMember cm2) {
        StringBuilder log = new StringBuilder();

        // Generate the dynamic threat using our scaling formula
        Threat threat = new Threat(completedMissions);
        log.append("=== MISSION: ").append(threat.getName()).append(" ===\n");
        log.append("Threat Level: ").append(completedMissions).append("\n");
        log.append("Threat Stats -> Power: ").append(threat.act())
                .append(" | HP: ").append(threat.getMaxEnergy()).append("\n\n");

        int round = 1;

        // Cooperative turn-based loop: Runs until the threat dies or both crew members are defeated
        while (!threat.isDefeated() && (cm1.getEnergy() > 0 || cm2.getEnergy() > 0)) {
            log.append("--- Round ").append(round).append(" ---\n");

            // --- CREW MEMBER 1'S TURN ---
            if (cm1.getEnergy() > 0) {
                threat.defend(cm1.act());
                log.append(cm1.getName()).append(" acts! Threat HP: ").append(threat.getEnergy()).append("\n");

                if (!threat.isDefeated()) {
                    cm1.defend(threat.act()); // Threat retaliates
                    log.append("Threat retaliates! ").append(cm1.getName()).append(" HP: ").append(cm1.getEnergy()).append("\n");

                    if (cm1.getEnergy() <= 0) {
                        log.append(">>> ").append(cm1.getName()).append(" WAS DEFEATED! <<<\n");
                        Storage.getInstance().removeCrewMember(cm1); // Permanent removal
                    }
                }
            }

            // --- CREW MEMBER 2'S TURN ---
            if (!threat.isDefeated() && cm2.getEnergy() > 0) {
                threat.defend(cm2.act());
                log.append(cm2.getName()).append(" acts! Threat HP: ").append(threat.getEnergy()).append("\n");

                if (!threat.isDefeated()) {
                    cm2.defend(threat.act()); // Threat retaliates
                    log.append("Threat retaliates! ").append(cm2.getName()).append(" HP: ").append(cm2.getEnergy()).append("\n");

                    if (cm2.getEnergy() <= 0) {
                        log.append(">>> ").append(cm2.getName()).append(" WAS DEFEATED! <<<\n");
                        Storage.getInstance().removeCrewMember(cm2); // Permanent removal
                    }
                }
            }
            log.append("\n");
            round++;
        }

        // --- MISSION RESOLUTION ---
        if (threat.isDefeated()) {
            completedMissions++; // Ramp up the difficulty for the next launch
            log.append("=== MISSION SUCCESS ===\nThe threat was neutralized!\n");

            // Process survivors: Award XP, move to Quarters, restore energy
            if (cm1.getEnergy() > 0) {
                cm1.gainExperience(3);
                cm1.setLocation("Quarters");
                cm1.restoreFullEnergy();
                log.append(cm1.getName()).append(" gained 3 XP and returned to Quarters.\n");
            }
            if (cm2.getEnergy() > 0) {
                cm2.gainExperience(3);
                cm2.setLocation("Quarters");
                cm2.restoreFullEnergy();
                log.append(cm2.getName()).append(" gained 3 XP and returned to Quarters.\n");
            }
        } else {
            log.append("=== MISSION FAILED ===\nAll assigned crew members were lost.\n");
        }

        return log.toString();
    }
}