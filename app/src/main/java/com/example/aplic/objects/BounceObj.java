package com.example.aplic.objects;

import android.content.Context;

import com.example.aplic.Statics;
// similar to CrossingSingleObj, but when it touches the bottom, it goes back up
public class BounceObj extends CrossingSingleObj {

    private boolean touchedBottom;

    public BounceObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
    }
    // same as CrossingSingleObj, but checks for collision with the ground
    @Override
    public void update() {
        if (pointGame.y >= Statics.height - height/2) {
            this.pointGame.velY *= -1;
            touchedBottom = true;
        }
        super.update();
    }

    @Override
    public void changeVelocities() {
        super.changeVelocities();
        if (touchedBottom)
            pointGame.velY = -pointGame.speed;
        else
            pointGame.velY = pointGame.speed;

    }
}
