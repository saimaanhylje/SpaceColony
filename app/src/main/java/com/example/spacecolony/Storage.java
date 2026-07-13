package com.example.spacecolony;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Storage instance = null;

    private static final String FILE_NAME = "colony_data.ser";

    // HashMap to seamlessly connect integer IDs to objects
    private HashMap<Integer, CrewMember> crewMap;

    // Starting completed missions at 0
    private int completedMissions = 0;

    private Storage() {
        crewMap = new HashMap<>();
    }

    public static synchronized Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void addCrewMember(CrewMember cm) {
        if (cm != null) {
            crewMap.put(cm.getId(), cm);
        }
    }

    // RESTORED: Needed by QuartersActivity to find selected crew by ID
    public CrewMember getCrewMember(int id) {
        return crewMap.get(id);
    }

    public void removeCrewMember(int id) {
        crewMap.remove(id);
    }

    // RESTORED: Needed by MissionControl for permadeath object removal
    public void removeCrewMember(CrewMember cm) {
        if (cm != null) {
            crewMap.remove(cm.getId());
        }
    }

    public ArrayList<CrewMember> getAllCrew() {
        return new ArrayList<>(crewMap.values());
    }

    public int getCompletedMissions() {
        return completedMissions;
    }

    public void incrementCompletedMissions() {
        this.completedMissions++;
    }

    public ArrayList<CrewMember> getCrewByLocation(String location) {
        ArrayList<CrewMember> filteredList = new ArrayList<>();
        for (CrewMember cm : crewMap.values()) {
            if (cm.getLocation().equals(location)) {
                filteredList.add(cm);
            }
        }
        return filteredList;
    }

    // Explicitly wipes out everything in active memory cleanly
    public void resetStorage() {
        this.crewMap.clear();
        this.completedMissions = 0;
    }

    // Saves BOTH the map data structure and the difficulty integer counter
    public void saveToFile(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(crewMap);          // 1. Write the map
            oos.writeInt(completedMissions);   // 2. Write the counter

            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads BOTH elements sequentially back into active state
    @SuppressWarnings("unchecked")
    public void loadFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            crewMap = (HashMap<Integer, CrewMember>) ois.readObject();
            completedMissions = ois.readInt();

            ois.close();
            fis.close();
        } catch (Exception e) {
            // If file doesn't exist yet, build a clean slate environment
            crewMap = new HashMap<>();
            completedMissions = 0;
        }
    }
}