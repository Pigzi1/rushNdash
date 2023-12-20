package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Canvas;

import com.example.aplic.Statics;
import com.example.aplic.activities.GameScreenActivity;

public class AlphaObj extends RectGame {
    private final float initVelY;

    public AlphaObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
        this.pointGame.x = rnd.nextInt(Statics.width);
        initVelY = (1 + rnd.nextFloat());
        changeVelocities();
        paint.setAlpha(255);
    }
    // reduces alpha when player is moving, increases it when player is not moving
    public void changeAlpha() {
        int currAlpha = this.paint.getAlpha();
        int newAlpha;
        Player player = ((GameScreenActivity) context).getGamePanel().getPlayer();
        if (player.getPointGame().velX == 0 && player.getPointGame().velY == 0)
            newAlpha = currAlpha + 25;
        else
            newAlpha = currAlpha - 25;
        newAlpha = Math.min (255, Math.max(0, newAlpha));
        this.paint.setAlpha(newAlpha);
    }

    @Override
    public void draw(Canvas canvas) {
        setRect(this.rect);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
        this.moveObst();
        this.changeAlpha();
    }

    @Override
    public void changeVelocities() {
        this.pointGame.velX = 0;
        this.pointGame.velY = this.pointGame.speed * initVelY;
    }
}
