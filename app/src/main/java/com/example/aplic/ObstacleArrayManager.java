package com.example.aplic;

import static com.example.aplic.Statics.user;

import android.content.Context;
import android.graphics.Canvas;

import com.example.aplic.activities.GameScreenActivity;
import com.example.aplic.objects.AlphaObj;
import com.example.aplic.objects.CrossingObj;
import com.example.aplic.objects.BounceObj;
import com.example.aplic.objects.CrossingSingleObj;
import com.example.aplic.objects.DownUpObj;
import com.example.aplic.objects.GapObj;
import com.example.aplic.objects.MovingGapObj;
import com.example.aplic.objects.MovingRectObj;
import com.example.aplic.objects.Player;
import com.example.aplic.objects.PositionSensorObj;
import com.example.aplic.objects.WarningObj;

import java.util.ArrayList;
import java.util.Random;

public class ObstacleArrayManager {

    private final Context context;
    private int randomClass;
    private final ArrayList<ObstacleArray> obstList;
    private final int levelID;

    public ObstacleArrayManager(Context context) {
        this.context = context;
        this.obstList = new ArrayList<>();
        this.levelID = ((GameScreenActivity) context).getLevelID();
    }
    // add an array of a randomly chosen object to the list
    public void addArrToList() {
        randomClass = getClassFromLevel(getArrayFromLevelID());
        ObstacleArray obstacleArray = setArray();
        obstList.add(obstacleArray);
    }
    // returns a random class from the array of objects that can appear in the level
    public int getClassFromLevel(int[] arr) {
        int rnd = new Random().nextInt(arr.length);
        return arr[rnd];
    }
    // makes the array of objects based on the random class it generated
    public ObstacleArray setArray() {
        ObstacleArray obstArr;
        switch (this.randomClass) {
            case 1: obstArr = new ObstacleArray(context, AlphaObj.class, setRndNum(2)); break;
            case 2: obstArr = new ObstacleArray(context, MovingRectObj.class, setRndNum(2)); break;
            case 3: obstArr = new ObstacleArray(context, GapObj.class, 1); break;
            case 4: obstArr = new ObstacleArray(context, WarningObj.class, 1); break;
            case 5: obstArr = new ObstacleArray(context, DownUpObj.class, setRndNum(1)); break;
            case 6: obstArr = new ObstacleArray(context, MovingGapObj.class,1); break;
            case 7: obstArr = new ObstacleArray(context, CrossingObj.class, 1); break;
            case 8: obstArr = new ObstacleArray(context, CrossingSingleObj.class,setRndNum(2)); break;
            case 9: obstArr = new ObstacleArray(context, BounceObj.class, setRndNum(2)); break;
            case 10: obstArr = new ObstacleArray(context, PositionSensorObj.class, setRndNum(1)); break;
            default: obstArr = new ObstacleArray(context, MovingRectObj.class,1); break;
        }
        return obstArr;
    }
    // sets the sleep time after the object spawns depending on the object that spawned
    public int getSleepFromType() {
        int sleepTime;
        switch (this.randomClass) {
            case 4: sleepTime = changeSleepFromDifficulty(1000); break;
            case 6:
            case 3: sleepTime = changeSleepFromDifficulty(1600); break;
            case 5:
            case 1: sleepTime = changeSleepFromDifficulty(2000); break;
            case 8: case 10:
            case 2: sleepTime = changeSleepFromDifficulty(2500); break;
            case 7 : sleepTime = changeSleepFromDifficulty(3000); break;
            case 9: sleepTime = changeSleepFromDifficulty(3500); break;
            default: sleepTime = changeSleepFromDifficulty(500000); break;
        }
        return sleepTime;
    }
    // changes the sleep time between spawning objects depending on the player's chosen difficulty
    public int changeSleepFromDifficulty(int originalSleep) {
        if (user.difficulty == 0)
            return (int)(originalSleep * 1.75);
        if (user.difficulty == 1)
            return originalSleep;
        if (user.difficulty == 2)
            return (int)(originalSleep*0.75);
        return originalSleep;
    }
    // checks if any of the objects in the list touched the player, returns yes if any of them did
    public boolean collisionAll (Player player) {
        for (ObstacleArray obstArr : obstList)
            if (obstArr.collision(player))
                return true;
        return false;
    }
    // draws all objects in the list on the canvas
    public void drawAll(Canvas canvas) {
        for (ObstacleArray obstacleArray : obstList) {
            obstacleArray.draw(canvas);
        }
    }
    // updates all objects in the list
    public void updateAll() {
        for (ObstacleArray obstacleArray: obstList) {
            obstacleArray.update();
        }
    }
    // changes speed to all objects in the list based on the type
    public void changeSpeedAll(int type) {
        for (ObstacleArray obstacleArray: obstList) {
            obstacleArray.changeSpeed(type);
        }
    }

    // sets the array of objects that can appear in the level
    public int[] getArrayFromLevelID() {
        int[] arr;
        switch (levelID) {
            // level 0 - play without signing in
            case 0: arr = new int[]{2}; break;
            case 1: arr = new int[]{1,2,3}; break;
            case 2: arr = new int[]{1,2,4}; break;
            case 3: arr = new int[]{1,3,4}; break;
            case 4: arr = new int[]{2,3,4}; break;
            case 5: arr = new int[]{1,2,3,4}; break;
            case 6: arr = new int[]{2,3,4,5,6}; break;
            case 7: arr = new int[]{4,5,6}; break;
            case 8: arr = new int[]{1,3,5,6}; break;
            case 9: arr = new int[]{1,2,4,6}; break;
            case 10: arr = new int[]{1,2,3,4,5,6}; break;
            case 11: arr = new int[]{1,4,5,7}; break;
            case 12: arr = new int[]{2,3,4,5,8}; break;
            case 13: arr = new int[]{2,4,6,7}; break;
            case 14: arr = new int[]{1,3,5,6,8}; break;
            case 15: arr = new int[]{1,2,3,4,5,6,7,8}; break;
            case 16: arr = new int[]{3,4,6,8,9}; break;
            case 17: arr = new int[]{1,2,5,7,9}; break;
            case 18: arr = new int[]{1,3,5,6,8,9}; break;
            case 19: arr = new int[]{6,7,8,9}; break;
            case 20: arr = new int[]{1,2,3,4,5,6,7,8,9}; break;
            case 21: arr = new int[]{1,2,3,4,10}; break;
            case 22: arr = new int[]{3,5,6,9,10}; break;
            case 23: arr = new int[]{4,5,6,7,10}; break;
            case 24: arr = new int[]{5,6,7,8,9,10}; break;
            case 25: arr = new int[]{1,2,3,4,5,6,7,8,9,10}; break;
            default: arr = new int[]{1000}; break;
        }
        return arr;
    }
    // returns a random number in a range to be the length of the array
    public int setRndNum(int type) {
        switch (type) {
            case 1: return 1 + new Random().nextInt(3);
            case 2: return 3 + new Random().nextInt(3);
            default: return 1;
        }
    }
}