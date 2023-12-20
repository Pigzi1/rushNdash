package com.example.aplic.activities;


import static android.content.ContentValues.TAG;


import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplic.GamePanel;
import com.example.aplic.R;
import com.example.aplic.VibrationService;

import java.io.IOException;

public class GameScreenActivity extends Activity implements View.OnClickListener {
    private TextView tvLoading, tvFPS;
    private GamePanel gamePanel;
    private FrameLayout frameLayout;
    private ImageView ivConnection;
    private ImageButton ibBack;
    private int levelID;
    private MediaPlayer mediaPlayer;
    private int[] iconImages, backgroundImages, bonusImages, powerUpImages;
    // saves the retry count so the power up won't save after a game ends
    private int retryCount = 0;
    private final BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: Connection changed");
            boolean isConnected = isConnectedToInternet(context);
            changeConnectionErrorVisibility(isConnected);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        levelID = getIntent().getIntExtra("LevelID", 0);
        Log.i(TAG, "onCreate: LevelID " + levelID);
        tvLoading = findViewById(R.id.tvLoading);
        tvFPS = findViewById(R.id.tvFPS);
        ivConnection = findViewById(R.id.ivConnection);
        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);
        // get images from HubActivity
        Bundle bitmapArrays = getIntent().getExtras();
        iconImages = bitmapArrays.getIntArray("icon_images");
        backgroundImages = bitmapArrays.getIntArray("background_images");
        bonusImages = bitmapArrays.getIntArray("bonus_images");
        powerUpImages = bitmapArrays.getIntArray("power_up_images");
        if (user.optMusic)
            prepareMusic();
        else
            addGamePanel();
    }
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    public int getLevelID() {
        return levelID;
    }
    public int getRetryCount() {
        return retryCount;
    }
    public int[] getPowerUpImages() {
        return powerUpImages;
    }
    // displays the FPS in the textview if the user chose for it to happen in the options
    @SuppressLint("SetTextI18n")
    public void setFPSView(double FPS) {
        if (user.optFPS) {
            // uses handler to post messages to the UI thread's message queue
            // which will also ensure that the code is executed on the UI thread
            // and not give an android.view.ViewRootImpl$CalledFromWrongThreadException
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> tvFPS.setText("FPS: " + FPS));
        }
    }
    // removes previous gamePanel and replace it with a new one
    public void onRetry(GamePanel newGamePanel) {
        frameLayout.removeView(this.gamePanel);
        frameLayout.addView(newGamePanel);
        this.gamePanel = newGamePanel;
        retryCount++;
    }
    // quits the game from the button
    @Override
    public void onClick(View view) {
        if (view == ibBack) {
            this.gamePanel.finishGame();
            this.finish();
        }
    }
    // adds a game panel and replaces the older one
    public void addGamePanel() {
        tvLoading.setVisibility(View.INVISIBLE);
        gamePanel = new GamePanel(this);
        frameLayout = findViewById(R.id.cvGame);
        frameLayout.addView(gamePanel);
    }
    public void prepareMusic() {
        mediaPlayer = new MediaPlayer();
        String url = "";
        // gets the songs url from firebase storage
        switch (levelID) {
            case 0: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FStayInsideMe.mp3?alt=media&token=882438ba-a2af-4e80-a508-bad98a899ffd"; break;
            case 1: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FPartytArIgang.mp3?alt=media&token=d4cf27f6-a449-436a-9f1c-e765fc8fe352"; break;
            case 2: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FVsSusie.mp3?alt=media&token=70d465f4-7512-4820-90bf-568dea1e02fd"; break;
            case 3: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FTheVengeanceOfThoseForgottenInDarkness.ogg?alt=media&token=9bfaff9f-92b6-4fca-8451-13e01800e40d"; break;
            case 4: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FDimension.mp3?alt=media&token=984bffef-9738-4c2b-b637-c02cf685828e"; break;
            case 5: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FYouWereWrongGoBack.ogg?alt=media&token=d6c84f65-554a-494c-a1cd-471c62e3a780"; break;
            case 6: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FPeerGynt.mp3?alt=media&token=cc1a7024-d596-4d1b-8db8-a5404c4bd469"; break;
            case 7: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FLostMemories.mp3?alt=media&token=38f140d1-9722-44a5-a548-4a5d814d92e3"; break;
            case 8: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FBreadySteadyGo.ogg?alt=media&token=650e5d7e-bb72-435e-a930-6169121aa6ae"; break;
            case 9: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FBetrayalOfFear.mp3?alt=media&token=16b24faa-c546-40da-b833-48f0178ae43b"; break;
            case 10: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FWorldsEndValentine.ogg?alt=media&token=2d2d29f4-3797-4e3b-8b23-50582530b0c6"; break;
            case 11: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FGhostHouse.mp3?alt=media&token=9817b9e6-268a-4d5d-8454-57d5178f4442"; break;
            case 12: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FMETALOVANIA.mp3?alt=media&token=b753a951-fc29-448d-bfb9-25012cd9c634"; break;
            case 13: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FSwirly1000x.ogg?alt=media&token=727491d0-d8a7-46af-8d88-290c1aaa9490"; break;
            case 14: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FKenos.mp3?alt=media&token=c5c7facd-fd6d-4ed1-8c3b-740de18ea322"; break;
            case 15: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FTeeHeeTime.ogg?alt=media&token=68a28c93-5067-489e-a931-135ac2a35f9e"; break;
            case 16: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FTheoryOfEverything3(DJNate).mp3?alt=media&token=e8ad7daa-c429-4313-a7d6-66df717fac44"; break;
            case 17: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FLingeringLightOfTheSettingSun.mp3?alt=media&token=dbc601a4-a2c1-46e6-957b-b1fde89f85d9"; break;
            case 18: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FItMeansEverything.ogg?alt=media&token=6ffa99c0-6394-4c04-a314-5c60c626b6bc"; break;
            case 19: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FSonicBlaster.mp3?alt=media&token=717a50b2-921b-48d3-ac0c-d01bbf157c65"; break;
            case 20: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FGoldenvengence.ogg?alt=media&token=b4813fa3-5ee5-4c92-a220-e060fe6f2381"; break;
            case 21: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FTheoryOfEverything3(domyeah).mp3?alt=media&token=68bf8298-9af0-4170-8273-c1d96b4978db"; break;
            case 22: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FDarkEnd.mp3?alt=media&token=bcc34058-f880-4eb9-87be-f4ede76c589e"; break;
            case 23: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FCantGoBack.ogg?alt=media&token=8fdeb4f0-512d-4775-a31a-917b5c96a810"; break;
            case 24: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FNineCircles.mp3?alt=media&token=0b66d5b0-02b3-4e73-b731-d03563cfc59c"; break;
            case 25: url = "https://firebasestorage.googleapis.com/v0/b/aplic-1.appspot.com/o/songs%2FPhobiaWave.mp3?alt=media&token=de78d25c-dda7-4e82-b9af-e7e273ebd858"; break;
        }
        try {
            //prepares the music
            Log.i(TAG, "prepareMusic: wow");
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                addGamePanel();
                playMusic();
                Log.i(TAG, "onPrepared: playing music");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // plays the music
    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            // replays the song when it is over
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            });
        }
    }
    // stops the music
    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    // turns on and off the connection symbol to alert the player if they have connection issues
    public void changeConnectionErrorVisibility(boolean connection) {
        if (connection) {
            Log.i(TAG, "changeConnectionErrorVisibility: " + true);
            ivConnection.setVisibility(View.INVISIBLE);
        }
        else  {
            Log.i(TAG, "changeConnectionErrorVisibility: " + false);
            ivConnection.setVisibility(View.VISIBLE);
        }
    }
    // broadcast receiver that changes the connection image view when connection status changes
    // and broadcast receiver
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
    // stops the broadcast receiver, the music, vibrates the phone
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityChangeReceiver);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        startService(new Intent(this, VibrationService.class));
    }
    // sets the bitmaps for the user from their selected items
    public int getBitmapID (int type) {
        // sets default values for when the player is a guest
        if (iconImages == null)
            switch (type) {
                case 1: return R.drawable.icon1;
                case 2: return R.drawable.background1;
                case 3: return R.drawable.bonus1;
                case 4: return 0;
            }
        switch (type) {
            case 1: return iconImages[user.selectedIcon];
            case 2: return backgroundImages[user.selectedBackground];
            case 3: return bonusImages[user.selectedBonus];
            case 4: return powerUpImages[user.selectedPowerUp];
        }
        return 0;
    }

}