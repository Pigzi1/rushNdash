package com.example.aplic.objects;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.example.aplic.Statics;

public class CrossingSingleObj extends RectGame{

    private int movingDirection;
    private final float makeRandomVelX;

    public CrossingSingleObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
        this.pointGame.x = width/2 + rnd.nextInt(-1*width + Statics.width);
        this.pointGame.y = -width/2;
        makeRandomVelX = this.pointGame.speed * (1 + rnd.nextFloat());
        Log.i(TAG, "CrossingSingleObj: " + makeRandomVelX);
        if (rnd.nextInt(2) == 0)
            movingDirection = 0;
        else
            movingDirection = 1;
        this.pointGame.velY = this.pointGame.speed;
    }

    @Override
    public void draw(Canvas canvas) {
        setRect(this.rect);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
        // checks for if the object touched a wall and changes its horizontal direction
        if (movingDirection == 0) {
            if (pointGame.x > Statics.width-this.width/2)
                movingDirection = 1;
        }
        if (movingDirection == 1) {
            if (pointGame.x < this.width/2)
                movingDirection = 0;
        }
        changeVelocities();
        this.moveObst();
    }

    @Override
    public void changeVelocities() {
        float newSpeed;
        switch (pointGame.speed) {
            case 0: newSpeed = 0; break;
            case 3: newSpeed = 3; break;
            default: newSpeed = makeRandomVelX; break;
        }
        pointGame.velY = pointGame.speed;
        if (movingDirection == 0) {
            pointGame.velX = newSpeed;
        }
        else if (movingDirection == 1) {
            pointGame.velX = -newSpeed;
        }
    }
}
