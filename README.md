Project Documentation

Developer: Ella Hakala
Development Environment: Android Studio (Java, Android SDK)

Overview

The project flow goes from creating new crew members to sending them to a mission or to a training simulator. The difficulty of the threat in the mission goes up as the number of completed missions goes up. If the crewmembers’ energy goes to zero during the mission, they will be removed permanently from the program. Otherwise, the crewmembers stay in the game and are saved to a file, and loaded from the file always when the application is run. The user can train the crewmembers so that their experience points will increase, and they will perform better during the missions.
The UML diagram was created with https://www.planttext.com/. It includes all classes, encapsulation signs (- for private, # for protected, + for public), data dependencies, and inheritance paths.

Phases in more detail

Creating Crew (RecruitActivity): 
The application starts by allowing the user to recruit crew members in RecruitActivity. You give them a name and choose their specialization (Pilot, Engineer, Medic, Scientist, or Soldier). When you click create, an image pops up showing a picture for that role, and they are saved with their base stats. The EditText box for entering the crewmembers name clears out for the user to continue creating more members, or they can go back to the menu by clicking “Go back” button. The newly created crewmember is automatically stationed at the home base location ("Quarters") and also saved to a file.

Home Quarters (QuartersActivity): All newly created crew members start at the home screen quarters, which are listed in a RecyclerView inside QuartersActivity. From here, the user can use checkboxes to select individual crew members and use buttons to move them to a new location: either "Simulator" or "Mission Control". This serves as the central operational terminal. Using a scrollable list, the interface reads the crewmembers from local memory and gives them vital stats (Role, Current/Max Energy, and XP). The user can select multiple crew members using the checkboxes. Once the location tag is changed, they filter out of the home quarter’s view to prevent assignment conflicts.

The Training Facility (SimulatorActivity): If crew members are moved to the training simulator, they disappear from the home quarters screen. Inside SimulatorActivity, the user can select them from a dropdown menu to train them. Training takes always 5 energy points away from them but gives them 2 experience points (XP). When training is ready, they automatically become available back in the home quarters with their energy fully refreshed (as stated by the project’s instructions). The event log will display the training outcome on the screen.

Launching Missions (MissionActivity & MissionControl): If crew members are moved to mission control, they also disappear from the home quarters screen and appear in the MissionActivity dropdown menus. The user selects two different crew members to send on a cooperative turn-based mission against a threat. To launch an operation, the user must select two distinct crew members currently deployed to the "Mission Control" location. The game logic then evaluates a dynamic, mathematical combat simulation. The two crew members work together to damage the threat based on their skills and XP. If the threat is not defeated immediately, it attacks back. If a crew member's energy goes to zero during the mission, it will be removed permanently from the program (permadeath). If they win, they gain 3 XP, their energy is restored, and they safely return to the home quarters.
•	Threat Escalation: A Threat instance is created. Its resilience, skill and energy are scaled based on a global completedMissions counter. The skill grows linearly, while energy grows up very fast and resilience goes up by two in every three rounds. The threat name is selected completely at random from a static string list collection during the setup inside MissionControl.
•	The Cooperative Turns Loop: In each round, available crew member combines their sum of skill points and experience level to damage the threat with act(). The threat will defend the attack based on the resilience. If the threat survives a turn, it retaliates against a random target, passing attack based on the skill level.

Code implementation

The program relies heavily on polymorphism. The class CrewMember is an abstract parent class that implements Serializable. It defines the core variables shared by everyone—like id, name, specialization, skill, resilience, energy, maxEnergy, experience, and location.
Specialized roles like Pilot, Soldier, Medic, Engineer, and Scientist inherit directly from CrewMember. They use super() in their constructors to set up their custom starting attributes without copying and pasting lines of data, making it easy to handle their stats during missions or training.
The code uses a Singleton design inside the Storage.java class. It has a private constructor so no other class can make a new instance of it, and the app accesses it globally via Storage.getInstance().
Inside Storage, all active crew elements are stored inside a HashMap<Integer, CrewMember>. This data structure maps every character's unique id number directly to their object reference, making it fast to fetch specific elements or look them up by their current location string ("Quarters", "Simulator", or "Mission Control") with the getCrewByLocation(String location) method.
Simulator.java and MissionControl.java are only for calculations. They don't look at or change views directly. They just take the CrewMember objects, run math for training costs or round combat turns, change the variables, and return a clean text string layout.
The matching UI layout classes (SimulatorActivity, MissionActivity) read those raw text streams and bind them directly to layout widgets like tvMissionLog.

Disclaimer on AI usage:
I used Google Gemini to assist with my coding. I asked the AI the following questions: “Why does not this work” and “How could I improve my code?”. Assistance was asked to create the RecyclerView more efficiently, and how to add the new activities also in the AndroidManifest.xml file.



Bonus features:
1.	RecyclerView: 
I have added the RecyclerView widget in the activity_quarters layout. Inside QuartersActivity.java I have created inner class CrewAdapter which extends RecyclerView.Adapter. It will display the crew member’s details by the onBindViewHolder method and binds a checkbox for all row items.

I have also created an inner class CrewViewHolder that extends RecyclerView.ViewHolder and is defined inside the CrewAdapter class. If we have a long list of crew members, scrolling can become laggy if the app has to constantly search by using findViewById() for every single item. The CrewViewHolder solves this by finding the layout elements just once when the row is created and holding on to them securely in memory

2.	Randomness in missions:
The threat name (asteroids, aliens,..) is selected randomly from a static string list.

3.	Data Storage & Loading:
The crew members are saved to a file when created, and their experience points are saved when they change. Once the app is opened again, the saved crew members are available. This is achieved by using Java Object Serialization, writing the data into a private app file on the phone storage disk (colony_data.ser) via an ObjectOutputStream. When MainActivity boots up, it reads this stream via an ObjectInputStream to load the crew back where the user left them. The player can also clear the game’s data, which will remove all the created crewmembers.

4.	Custom Feature 2 points:
When crewmembers are moved to mission or to the training simulator, they disappear from the home screen. When the training or mission is ready, they become again available in the home quarters. The location of the crewmembers can be checked by getLocation method and changed by setLocation method. This prevents the player from accidentally sending the same character from home quarters to two different jobs at the same time. 

5.	Crew Images:
When creating a crewmember a picture depicting their specialization will pop up.
