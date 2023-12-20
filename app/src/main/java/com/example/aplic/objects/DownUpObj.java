package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Canvas;

import com.example.aplic.Statics;

public class DownUpObj extends RectGame {

    private boolean touchedBottom;

    public DownUpObj(Context context, String color, int width, int height) {
        super(context,color, width, height);
        this.pointGame.x = width/2 + rnd.nextInt(-1*width + Statics.width);
        this.pointGame.y = -this.width/2 - rnd.nextInt(200);
        this.pointGame.velX = 0;
        this.pointGame.velY = pointGame.speed;
    }
    
    @Override
    public void draw(Canvas canvas) {
        setRect(this.rect);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
        this.moveObst();
        // checks for ground collision and change the direction of the vertical velocity when detects it
        if (pointGame.y >= Statics.height - height/2) {
            this.pointGame.velY *= -1;
            touchedBottom = true;
        }
    }

    @Override
    public void changeVelocities() {
        if (touchedBottom)
            pointGame.velY = -pointGame.speed;
        else
            pointGame.velY = pointGame.speed;

    }
}
