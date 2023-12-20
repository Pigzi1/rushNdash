package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;

import com.example.aplic.Statics;

public class BonusObj extends RectGame {
    private int lowestY;
    private int status;
    final Handler handler;

    public BonusObj(Context context, int bitmapID, int width, int height) {
        super(context, bitmapID, width, height);
        handler = new Handler(Looper.getMainLooper());
        status = 0;
        this.pointGame.velX = 0;
        this.pointGame.velY = pointGame.speed;

    }

    public void setStatus(int status) {
        this.status = status;
    }
    // puts the object slightly above the screen on a random X value
    // gives it a random value that will be the lowest point it can be for "case 2" in update method
    public void setRandomPoint() {
        lowestY = Statics.height/5 +  rnd.nextInt(3* Statics.height/5);
        this.pointGame.x = width/2 + rnd.nextInt(-width + Statics.width);
        this.pointGame.y = -this.width/2;

    }

    public int setSleepTime() {
        return (5000 + rnd.nextInt(20000));
    }

    @Override
    public void draw(Canvas canvas) {
        if(status == 2 || status == 3 || status == 4) {
            canvas.drawBitmap(bitmap, this.pointGame.x - (float) this.width / 2, this.pointGame.y - (float) this.height / 2, null);
            setRect(this.rect);
        }
    }

    @Override
    public void update() {
        switch (status) {
            // change status to 1, then activate handler once and wait to be respawned,
            case 0:
                status++;
                handler.postDelayed(this::respawn, setSleepTime()); break;
            // waiting to be respawned, handler will make status go to 2
            case 1: break;
            // object is moving and detecting lowest point, change to 3
            case 2:
                moveObst();
                if (this.pointGame.y >= lowestY) {
                    status ++;
                } break;
            // keeps the object at lowest y, despawn it if not touched for the delay time
            case 3:
                status++;
                handler.postDelayed(() -> {
                    if (status > 2) {
                        kill();
                        respawn();
                    }
                }, 7000); break;
            // object not moving
            case 4: break;
        }
    }

    // respawns the object when the player touches it or when enough time passed and the player didn't touch it
    public void respawn() {
        setRandomPoint();
        if (status == 1)
            status++;
        else
            status = 0;
    }
    @Override
    public void changeVelocities() {

    }
}
