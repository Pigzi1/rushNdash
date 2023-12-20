package com.example.aplic.dialogs;

import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplic.GamePanel;
import com.example.aplic.activities.GameScreenActivity;
import com.example.aplic.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameOverDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final GameScreenActivity activity;
    private TextView tvNewHighScore;
    private Button btBack, btRetry;
    private final int score;
    private final String time;
    private final int levelID;

    public GameOverDialog(Context context, int score, String time, int levelID) {
        super(context);
        this.context = context;
        this.activity = (GameScreenActivity) context;
        this.score = score;
        this.time = time;
        this.levelID = levelID;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_game_over);
        setCancelable(false);

        tvNewHighScore = findViewById(R.id.tvNewHighScore);
        btBack = findViewById(R.id.btBack);
        btRetry = findViewById(R.id.btRetry);
        btBack.setOnClickListener(this);
        btRetry.setOnClickListener(this);
        // displays the score or the time depending on the type of the level
        TextView tvDisplay = findViewById(R.id.tvDisplay);
        if (levelID % 5 != 0) {
            tvDisplay.setText("Total Score: " + score);
        }
        else
            tvDisplay.setText("TIME LEFT: " + time);
        tryToSetNewProgression();
    }

    @Override
    public void onClick(View view) {
        // a go back button
        if (view == btBack) {
            this.dismiss();
            this.activity.finish();
        }

        // a retry button
        else if (view == btRetry) {
            if (isConnectedToInternet(context)) {
                this.dismiss();
                GamePanel newGamePanel = new GamePanel(activity);
                this.activity.onRetry(newGamePanel);
                activity.playMusic();
                activity.getGamePanel().getPowerUp().restartPowerUpTimer();
            }
            else
                Toast.makeText(context, "Please connect to the internet first", Toast.LENGTH_SHORT).show();
        }
    }
    // checks for if the new score is better than the old score
    public void tryToSetNewProgression() {
        if (isConnectedToInternet(context)) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.key);
            user.deaths++;
            userRef.child("deaths").setValue(user.deaths);
            if (levelID % 5 != 0 && score > user.hsl.get(levelID)) {
                user.hsl.set(levelID, score);
                userRef.child("hsl").child(String.valueOf(levelID)).setValue(score);
                displayNewMessage(true);
            }
            else if (levelID % 5 == 0 && isBestTime(user.bestTime.get(levelID/5))) {
                user.bestTime.set(levelID/5, time);
                userRef.child("bestTime").child(String.valueOf(levelID/5)).setValue(time);
                displayNewMessage(false);
            }
        }
        else
            Toast.makeText(context, "Please reconnect to the internet! \n progression will not be saved without internet connection! ", Toast.LENGTH_SHORT).show();
    }
    // checks for if the new time is better than the old time
    public boolean isBestTime(String oldBestTime) {
        String[] timeSplit = time.split(":");
        String[] oldBestTimeSplit = oldBestTime.split(":");
        int minute1 = Integer.parseInt(timeSplit[0]);
        int second1 = Integer.parseInt(timeSplit[1]);
        int minute2 = Integer.parseInt(oldBestTimeSplit[0]);
        int second2 = Integer.parseInt(oldBestTimeSplit[1]);

        return (minute1 < minute2 || (minute1 == minute2 && second1 < second2));
    }
    // displays a text to the player if they got a new high score or a better time
    @SuppressLint("SetTextI18n")
    public void displayNewMessage(boolean type) {
        // true = normal, false = boss
        if (type)
            tvNewHighScore.setText("New High Score!");
        else
            tvNewHighScore.setText("New Best Time!");
        tvNewHighScore.setVisibility(View.VISIBLE);
    }
}
