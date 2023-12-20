package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import com.example.aplic.Statics;

public class MovingRectObj extends RectGame {

    private final int type;
    private final float rndFX = rnd.nextFloat();
    private final float rndFY = 1 + rnd.nextFloat();
    private boolean rightSide;
    private int horizontalMovement;

    public MovingRectObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
        // this object will be randomly chosen as 1 of the 3: "Diagonal Obstacle", "Vertical Obstacle", "Horizontal Obstacle"
        // type: 0 = diagonal from top, 1 = vertical from top, 2 = horizontal from either sides
        type = rnd.nextInt(3);
        switch (type) {
            case 0:
                this.pointGame.y = -this.height;
                this.pointGame.x = -width + rnd.nextInt(2*width + Statics.width);
                if (this.pointGame.x >= Statics.width/2)
                    rightSide = true;
                break;
            case 1:
                this.paint.setColor(Color.parseColor("#9600FF"));
                this.pointGame.y = -this.height;
                this.pointGame.x = rnd.nextInt(Statics.width);
                break;
            case 2:
                this.paint.setColor(Color.parseColor("#646464"));
                horizontalMovement = rnd.nextInt(2);
                if (horizontalMovement == 0) {
                    this.pointGame.x = -2 * width;
                }
                else {
                    this.pointGame.x = Statics.width + 2 * width;
                }
                this.pointGame.y = rnd.nextInt(Statics.height);
                break;
        }
        changeVelocities();
    }

    // makes it so if the object spawned on the right side, it will move left horizontally and vice versa
    public void setDiagonalVelocity() {
        this.pointGame.velX = rndFX * this.pointGame.speed;
        this.pointGame.velY = rndFY * this.pointGame.speed;
        if (rightSide) {
            this.pointGame.velX *= -1;
        }
    }
    // sets the velocity for vertical points
    public void setVerticalVelocity() {
        this.pointGame.velX = 0;
        this.pointGame.velY = this.pointGame.speed;
    }
    // sets the velocity of the horizontal object based on if it should go left or right
    public void setHorizontalVelocity() {
        this.pointGame.velX = this.pointGame.speed;
        if (horizontalMovement == 1) {
            this.pointGame.velX *= -1;
        }
    }
    @Override
    public void draw(Canvas canvas) {
        setRect(this.rect);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
        this.moveObst();
    }

    @Override
    public void changeVelocities() {
        switch (type) {
            case 0: setDiagonalVelocity(); break;
            case 1: setVerticalVelocity(); break;
            case 2: setHorizontalVelocity(); break;
        }

    }
}
