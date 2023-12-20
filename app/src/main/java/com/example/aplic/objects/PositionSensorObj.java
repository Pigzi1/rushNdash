package com.example.aplic.objects;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.aplic.Statics;
import com.example.aplic.activities.GameScreenActivity;

public class PositionSensorObj extends RectGame {
    public PositionSensorObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
        pointGame.velY = pointGame.speed;
        pointGame.x = this.width/2 + rnd.nextInt(Statics.width + this.width);

    }
    @Override
    public void draw(Canvas canvas) {
        setRect(this.rect);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
        // moves the object's x value based on the phone's rotation
        float deltaX = (float) (0.3 * pointGame.speed * ((GameScreenActivity) context).getGamePanel().getGameSensors().getXRotation());
        // makes it so the object can't move out of the screen
        this.pointGame.x = (int)Math.min(Statics.width-this.width/2f, Math.max(this.width/2f, this.pointGame.x - deltaX));
        this.pointGame.y += pointGame.velY;
    }
    @Override
    public void changeVelocities() {
        pointGame.velY = pointGame.speed;
    }

}
