package com.example.aplic;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

// a class that has the information for every user that is saved in firebase
@IgnoreExtraProperties
public class User {
    public String key;
    public String uid;
    public String userName, mail;
    public int deaths;
    public int coins;
    public int difficulty; // 0 - easy, 1 - normal, 2 - hard
    // options
    public boolean optMusic, optSoundEffects, optFPS;
    // saves high score for every normal level
    public List<Integer> hsl;
    // saves best time for boss levels
    public List<String> bestTime;
    // saves if boss level was already beaten
    public ArrayList<Boolean> levelComplete;
    // boolean for if item is owned
    public ArrayList<Boolean> iconOwned, backgroundOwned, bonusOwned, powerUpOwned;
    // saves the selected item from the shop
    public int selectedIcon, selectedBackground, selectedBonus, selectedPowerUp;
    public int itemsOwned;

    public User() {
        InitializeAllValues();
    }

    public User(String uid, String mail, String userName, String key) {
        this.key = key;
        this.uid = uid;
        this.userName = userName;
        this.mail = mail;
        InitializeAllValues();
    }

    public void InitializeAllValues() {
        deaths = 0;
        coins = 0;
        itemsOwned = 0;
        difficulty = 1;
        optMusic = true; optSoundEffects = true; optFPS = false;

        hsl = new ArrayList<>();
        for (int i = 0; i <= 25; i++) {
            hsl.add(0);
        }
        bestTime = new ArrayList<>();
        bestTime.add(""); // index 0 is unused
        bestTime.add("1:16");bestTime.add("2:14");
        bestTime.add("1:44");bestTime.add("2:17");bestTime.add("1:25");
        levelComplete = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            levelComplete.add(false);
        }
        levelComplete.set(0, true); // index 0 is unused

        iconOwned = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            iconOwned.add(false);
        } iconOwned.set(0, true);

        backgroundOwned = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            backgroundOwned.add(false);
        } backgroundOwned.set(0, true);

        bonusOwned = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            bonusOwned.add(false);
        } bonusOwned.set(0, true);

        powerUpOwned = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            powerUpOwned.add(false);
        } powerUpOwned.set(0, true);


        selectedIcon = 0; selectedBackground = 0; selectedBonus = 0; selectedPowerUp = 0;

    }
}
