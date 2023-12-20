package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.aplic.Statics;

// the object that the player controls
public class Player extends RectGame {
    public Player(Context context, int bitmapID, int width, int height) {
        super(context, bitmapID, width, height);
        pointGame = new PointGame(Statics.width/2, (Statics.height*4)/5);
        pointGame.velX = 0; pointGame.velY = 0; pointGame.speed = 17;
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, this.pointGame.x - (float) this.width / 2, this.pointGame.y - (float) this.height / 2, null);
        pointGame.velX = 0; pointGame.velY = 0;
    }
    public void createBitmap() {
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
    @Override
    public void update() {
        setRect(this.rect);
    }
    @Override
    public void changeVelocities() {
    }
}
