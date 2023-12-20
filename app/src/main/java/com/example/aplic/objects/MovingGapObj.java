package com.example.aplic.objects;

import android.content.Context;

import com.example.aplic.Statics;

public class MovingGapObj extends GapObj {
    // 0 - left 1 - right
    private int movingDirection;
    public MovingGapObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
        movingDirection = rnd.nextInt(2);
        this.width *= (0.25 + (1+ rnd.nextFloat()));
    }

    @Override
    public void update() {
        super.update();
        // also checks for if the "fake" rectangle is about to hit the wall, and change its horizontal moving direction
        if (movingDirection == 0) {
            pointGame.velX = -pointGame.speed;
            if (pointGame.x < 300) {
                movingDirection = 1;
            }
        }
        if (movingDirection == 1) {
            pointGame.velX = pointGame.speed;
            if (pointGame.x > Statics.width-300) {
                movingDirection = 0;
            }
        }
    }

    @Override
    public void changeVelocities() {
        pointGame.velY =pointGame.speed;
        if (movingDirection == 0) {
            pointGame.velX = -pointGame.speed;
        }
        else if (movingDirection == 1) {
            pointGame.velX = pointGame.speed;
        }
    }
}
