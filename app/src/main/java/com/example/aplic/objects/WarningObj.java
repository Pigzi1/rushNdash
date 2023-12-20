package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;

import com.example.aplic.R;
import com.example.aplic.Statics;
import com.example.aplic.activities.GameScreenActivity;

public class WarningObj extends RectGame {
    private Bitmap warning;
    private int status = 0;
    private boolean soundPlayed = false;
    public WarningObj(Context context, int bonusID, int width, int height) {
        super(context, bonusID, width, height);
        warning = BitmapFactory.decodeResource(context.getResources(), R.drawable.warning_warn);
        warning = Bitmap.createScaledBitmap(warning, width, height, true);
        this.pointGame.x = width/2 + rnd.nextInt(-1*width + Statics.width);
        this.pointGame.y = height/2 + rnd.nextInt(-1*height + Statics.height);
    }

    @Override
    public void draw(Canvas canvas) {
        // firstly, warn the player by putting warning_warn on a random point on the screen
        if (status == 0) {
            canvas.drawBitmap(warning, this.pointGame.x - (float) this.width / 2, this.pointGame.y - (float) this.height / 2, null);
            // wait for 1.5 seconds
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                status = 1;
            }, 1500);
        }
        // play explosion sound effect, draw the object on the canvas and change the image to warning_active
        if (status == 1) {
            GameScreenActivity gsa =  (GameScreenActivity) context;
            if (!soundPlayed) {
                gsa.getGamePanel().getSFX().playSound("explosion");
                soundPlayed = true;
            }
            setRect(this.rect);
            canvas.drawBitmap(bitmap, this.pointGame.x - (float) this.width / 2, this.pointGame.y - (float) this.height / 2, null);
            final Handler handler = new Handler(Looper.getMainLooper());
            // wait for 3 seconds and then the object disappears
            handler.postDelayed(() -> {
                status = 2;
                kill();
            }, 3000);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void changeVelocities() {

    }
}
