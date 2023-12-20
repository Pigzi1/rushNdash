package com.example.aplic.objects;


import static com.example.aplic.Statics.user;

import android.graphics.Point;


import java.util.Random;

public class PointGame extends Point {
    protected float velX, velY;
    protected int speed;
    protected int initialSpeed;

    public PointGame(int x, int y) {
        super(x, y);
        setSpeedFromDifficulty();
    }

    public float getVelX() {
        return velX;
    }
    public void setVelX(float velX) {
        this.velX = velX;
    }
    public float getVelY() {
        return velY;
    }
    public void setVelY(float velY) {
        this.velY = velY;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    // each point will have a speed based on the player's chosen difficulty
    public void setSpeedFromDifficulty() {
        switch (user.difficulty) {
            case 0: speed = 10; break;
            case 1: default: speed = 15; break;
            case 2: speed = 20; break;
        }
        initialSpeed = speed;
    }
}
