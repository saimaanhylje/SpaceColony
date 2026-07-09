package com.example.spacecolony;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;

public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Storage instance = null;

    private static final String FILE_NAME = "colony_data.ser";

    //HashMap to seamlessly connect integer IDs to objects
    private HashMap<Integer, CrewMember> crewMap;

    //starting completedmission at 0:
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

    public CrewMember getCrewMember(int id) {
        return crewMap.get(id);
    }

    // Helper utility converting data seamlessly for UI components
    public ArrayList<CrewMember> getAllCrew() {
        return new ArrayList<>(crewMap.values());
    }

    // Returns only the crew currently in a specific location as an ArrayList

    public ArrayList<CrewMember> getCrewByLocation(String location) {
        ArrayList<CrewMember> filteredList = new ArrayList<>();

        // Loop through all the CrewMember objects stored in the values of the HashMap
        for (CrewMember cm : crewMap.values()) {
            if (cm.getLocation().equals(location)) {
                filteredList.add(cm);
            }
        }
        return filteredList;
    }

    // Permanently deletes a crew member from the program using their ID as the key
    public void removeCrewMember(CrewMember cm) {
        if (cm != null) {
            crewMap.remove(cm.getId());
        }
    }

    public void saveToFile(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(crewMap);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Called when the app starts
    @SuppressWarnings("unchecked")
    public void loadFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            crewMap = (HashMap<Integer, CrewMember>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            // File might not exist yet on first launch, that's okay
            crewMap = new HashMap<>();
        }
    }

}