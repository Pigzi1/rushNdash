package com.example.aplic;

import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplic.activities.GameScreenActivity;

public class PowerUp implements View.OnClickListener {

    private final Context context;
    private final GamePanel gamePanel;
    private final ImageButton ibPowerUp;
    private final int imageDisplayID;
    private final TextView tvPowerUpCoolDown;
    private int time, timeActive, timeCoolDown;
    // retries makes each runnable only work when the game is running
    private int retries = 0;

    public PowerUp(Context context, GamePanel gamePanel, ImageButton ibPowerUp) {
        this.context = context;
        this.gamePanel = gamePanel;
        this.ibPowerUp = ibPowerUp;
        ibPowerUp.setOnClickListener(this);
        imageDisplayID = user.selectedPowerUp;
        if (imageDisplayID == 0) {
            ibPowerUp.setVisibility(View.GONE);
        }
        else {
            @SuppressLint("UseCompatLoadingForDrawables") Drawable drawable = context.getResources().getDrawable(((GameScreenActivity) context).getPowerUpImages()[user.selectedPowerUp], null);
            Bitmap imageBitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 100,100, false);
            ibPowerUp.setImageBitmap(resizedBitmap);
        }
        tvPowerUpCoolDown = ((GameScreenActivity) context).findViewById(R.id.tvCountDown);
        setTimesFromType();
    }
    // sets the amount of time that the power up is active depending on the chosen power up
    public void setTimesFromType() {
        switch (imageDisplayID) {
            case 1: timeActive = 4; timeCoolDown = 20; break;
            case 2: timeActive = 3; timeCoolDown = 17; break;
            case 3: timeActive = 5; timeCoolDown = 15; break;
        }
    }
    // activates or deactivates the power up
    public void powerActivated(boolean activateOrDeactivate) {
        // param: true is to activate, false to deactivate
        switch (imageDisplayID) {
            // player higher speed
            case 1:
                if (activateOrDeactivate) {
                    gamePanel.getPlayer().getPointGame().setSpeed(75);
                    gamePanel.setNotNormalSpeed(true);
                }
                else {
                    gamePanel.getPlayer().getPointGame().setSpeed(17);
                    gamePanel.setNotNormalSpeed(false);
                }
                break;
            // player smaller
            case 2:
                if (activateOrDeactivate) {
                    gamePanel.getPlayer().setWidth(50);
                    gamePanel.getPlayer().setHeight(50);
                } else {
                    gamePanel.getPlayer().setWidth(100);
                    gamePanel.getPlayer().setHeight(100);
                }
                gamePanel.getPlayer().createBitmap();
                break;
            // obstacles move slower
            case 3:
                if (activateOrDeactivate) {
                    gamePanel.getObstacleArrayManager().changeSpeedAll(1);
                    gamePanel.setNotNormalSpeed(true);
                }
                else {
                    gamePanel.getObstacleArrayManager().changeSpeedAll(3);
                    gamePanel.setNotNormalSpeed(false);
                } break;

        }
    }
    // activate power up and timer when clicked
    @Override
    public void onClick(View view) {
        // checks for if retries need to be updated
        retries = ((GameScreenActivity) context).getRetryCount();
        // if the speed already changed from the bonus object, the player cannot activate the freeze power up
        if (gamePanel.isNotNormalSpeed() && imageDisplayID == 3) {
            Toast.makeText(context, "Cannot activate this power up when objects are frozen", Toast.LENGTH_SHORT).show();
            return;
        }
        powerActivated(true);
        ibPowerUp.setClickable(false);
        ibPowerUp.setVisibility(View.INVISIBLE);
        time = timeActive + timeCoolDown;
        startTime();
        Log.i("TAG", "onClick: activated");
    }
    // changes the color of the text depending on the type of timer
    public void setPowerUpTextColor(boolean activateOrDeactivate) {
        if (activateOrDeactivate)
            tvPowerUpCoolDown.setTextColor(Color.rgb(11,102,35));
        else
            tvPowerUpCoolDown.setTextColor(Color.WHITE);
    }
    // starts the timer
    @SuppressLint("SetTextI18n")
    public void startTime() {
        if (time == 0) {
            tvPowerUpCoolDown.setText("");
            this.ibPowerUp.setClickable(true);
            this.ibPowerUp.setVisibility(View.VISIBLE);
        }
        else {
            // displays time as a text, if statement is to check if power up is active or on cool down
            if (time > timeCoolDown) {
                setPowerUpTextColor(true);
                tvPowerUpCoolDown.setText(Integer.toString(time - timeCoolDown));
            }
            else {
                if (time == timeCoolDown)
                    powerActivated(false);
                setPowerUpTextColor(false);
                tvPowerUpCoolDown.setText(Integer.toString(time));
            }
            // wait a second and reduce time by 1 second, call for the function again
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (retries == ((GameScreenActivity) context).getRetryCount()) {
                    if (time != 0) {
                        time--;
                        startTime();
                    }
                }
            }, 1000);
        }
    }
    // when the game restarts, the function restarts the timer that show the power ups's cool down
    public void restartPowerUpTimer() {
        tvPowerUpCoolDown.setText("");
        if (user.selectedPowerUp != 0) {
            ibPowerUp.setClickable(true);
            ibPowerUp.setVisibility(View.VISIBLE);
        }
        time = 0;
    }
}
