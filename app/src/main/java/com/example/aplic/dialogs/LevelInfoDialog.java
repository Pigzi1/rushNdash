package com.example.aplic.dialogs;

import static android.content.ContentValues.TAG;
import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.aplic.R;
import com.example.aplic.activities.GameScreenActivity;

public class LevelInfoDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final int levelID;
    private final Bundle bitmapArrays;
    private Button btSong, btClose, btPlay;

    public LevelInfoDialog(Context context, int levelID, Bundle bitmapArrays) {
        super(context);
        this.context = context;
        this.levelID = levelID;
        this.bitmapArrays = bitmapArrays;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_level_info);
        setCancelable(false);

        TextView tvLevel = findViewById(R.id.tvLevel);
        tvLevel.setText("LEVEL " + levelID);
        TextView tvScoreOrTime = findViewById(R.id.tvScoreOrTime);
        btSong = findViewById(R.id.btSong);
        btClose = findViewById(R.id.btClose);
        btPlay = findViewById(R.id.btPlay);
        btPlay.setOnClickListener(this);
        btClose.setOnClickListener(this);
        btSong.setOnClickListener(this);
        // checks for if previous level is done
        if (isPrevHighScoreOrCompleted()) {
            switch (levelID) {
                // sets the song text and the text for score or time
                case 0: default: btSong.setText("Song - Stay Inside Me - OcularNebula"); break;
                case 1: btSong.setText("Song - Partyt Är Igång! - Bossfight"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(1)); break;
                case 2: btSong.setText("Song - Vs Susie - NyxTheShield"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(2)); break;
                case 3: btSong.setText("Song - The Vengeance Of Those Forgotten In Darkness - Jami Lynne"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(3)); break;
                case 4: btSong.setText("Song - Dimension - Creo"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(4)); break;
                case 5: btSong.setText("Song - You Were Wrong. Go Back - Jami Lynne"); tvScoreOrTime.setText(setBossLevelText()); break;
                case 6: btSong.setText("Song - Peer Gynt - cYsmix"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(6)); break;
                case 7: btSong.setText("Song - Lost Memories - MULTI BGM STUDIO"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(7)); break;
                case 8: btSong.setText("Song - Bready Steady Go - Pedro Silva"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(8)); break;
                case 9: btSong.setText("Song - Betrayal Of Fear - Goukisan"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(9)); break;
                case 10: btSong.setText("Song - World's End Valentine - Pedro Silva"); tvScoreOrTime.setText(setBossLevelText()); break;
                case 11: btSong.setText("Song - Ghost House - schtiffles"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(11)); break;
                case 12: btSong.setText("Song - METALOVANIA - Metal Socks"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(12)); break;
                case 13: btSong.setText("Song - Swirly 1000x - Pedro Silva"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(13)); break;
                case 14: btSong.setText("Song - Iron God Sakupen Hell Yes RMX - mr-jazzman"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(14)); break;
                case 15: btSong.setText("Song - Tee-Hee Time - Pedro Silva"); tvScoreOrTime.setText(setBossLevelText()); break;
                case 16: btSong.setText("Song - Theory Of Everything 3 - DJ Nate"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(16)); break;
                case 17: btSong.setText("Song - Lingering Light Of The Setting Sun - Snow"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(17)); break;
                case 18: btSong.setText("Song - It Means Everything - Pedro Silva"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(18)); break;
                case 19: btSong.setText("Song - Sonic Blaster - F-777"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(19)); break;
                case 20: btSong.setText("Song - Goldenvengeance - Jami Lynne"); tvScoreOrTime.setText(setBossLevelText()); break;
                case 21: btSong.setText("Song - Theory Of Everything - Domyeah"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(21)); break;
                case 22: btSong.setText("Song - Dark_End - RsnowJ"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(22)); break;
                case 23: btSong.setText("Song - You Can't Go Back - Jami lynne"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(23)); break;
                case 24: btSong.setText("Song - Nine Circles - NightKilla"); tvScoreOrTime.setText("Highest Score: " + user.hsl.get(24)); break;
                case 25: btSong.setText("Song - Phobiawave - Jami Lynne"); tvScoreOrTime.setText(setBossLevelText()); break;
            }
            btSong.setVisibility(View.VISIBLE);
            tvScoreOrTime.setVisibility(View.VISIBLE);
            btPlay.setVisibility(View.VISIBLE);
        }
        else {
            // sets the text for requirements
            TextView tvShowReq = findViewById(R.id.tvShowReq);
            tvShowReq.setVisibility(View.VISIBLE);
            TextView tvReq = findViewById(R.id.tvReq);
            tvReq.setText(requirements());
            tvReq.setVisibility(View.VISIBLE);
        }
    }
    // checks for if previous level is done
    public boolean isPrevHighScoreOrCompleted() {
        if (levelID % 5 == 1) {
            return user.levelComplete.get((levelID-1)/5);
        }

        try {
            return (user.hsl.get(levelID - 1) >= scoreReq());
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }
    // set the text to show best time a player's had on a boss level
    public String setBossLevelText() {
        if (user.levelComplete.get(levelID/5))
            return "Level Completed";
        else return "Lowest Time: " + user.bestTime.get(levelID/5);
    }
    // returns the required points to beat the previous level from the number of level
    public int scoreReq() {
        return 3000 + (levelID - 1) * 1000;
    }
    // tells the player the requirements to unlocking the level if it is not unlocked
    public String requirements() {
        if (levelID % 5 != 1) {
            int score = scoreReq();
            return "- All previous levels unlocked \n- Score of " + score + " on level " + (levelID - 1);
        }
        return "- All previous levels unlocked \n- Complete world " + (levelID - 1) / 5;
    }
    @Override
    public void onClick(View view) {
        if (view == btClose)
            // close
            dismiss();
        else if (view == btPlay) {
            if (isConnectedToInternet(context)) {
                // starts the game activity and adds the bitmaps to it's extras
                Intent intent = new Intent(view.getContext(), GameScreenActivity.class);
                intent.putExtra("LevelID", levelID);
                Log.i(TAG, "onClick: LEVEL - " + levelID);
                intent.putExtras(bitmapArrays);
                context.startActivity(intent);
                dismiss();
            }
            else
                Toast.makeText(context, "Please connect to a stable internet connection", Toast.LENGTH_SHORT).show();
        }
        else if (view == btSong) {
            String url;
            // changes url to the song link from youtube
            switch (levelID) {
                case 0: default: url = "https://youtu.be/ix6e9h40qMg"; break;
                case 1: url = "https://youtu.be/AECcMhgSEsQ"; break;
                case 2: url = "https://youtu.be/eJHsEmtWiyM"; break;
                case 3: url = "https://youtu.be/9hqWe-Fr4ko"; break;
                case 4: url = "https://youtu.be/MrD05HVGVIQ"; break;
                case 5: url = "https://youtu.be/TyHXvJdyMUo"; break;
                case 6: url = "https://youtu.be/w4dLTLW6dJ0"; break;
                case 7: url = "https://youtu.be/qN56222aBWQ"; break;
                case 8: url = "https://youtu.be/QL_MA9kQ-0o"; break;
                case 9: url = "https://youtu.be/eXv0tqBtv3E"; break;
                case 10: url = "https://youtu.be/rlQd9qWKjLM"; break;
                case 11: url = "https://youtu.be/UHGvNoRPCQA"; break;
                case 12: url = "https://youtu.be/H5rbS0VuX1o"; break;
                case 13: url = "https://youtu.be/Hww-EBjdKvg"; break;
                case 14: url = "https://youtu.be/nTUVLuKAAkA"; break;
                case 15: url = "https://youtu.be/n9gm9lP_4Gk"; break;
                case 16: url = "https://youtu.be/fiAz__CBDIU"; break;
                case 17: url = "https://youtu.be/6VXd_Axc4oc"; break;
                case 18: url = "https://youtu.be/8wKWwUzgCE8"; break;
                case 19: url = "https://youtu.be/IvYD-HLAl8E"; break;
                case 20: url = "https://youtu.be/PqQC_gl5k0w"; break;
                case 21: url = "https://youtu.be/0T3XU6zh5No"; break;
                case 22: url = "https://youtu.be/qhKDQlPfsJM"; break;
                case 23: url = "https://youtu.be/Pe---DN2TmY"; break;
                case 24: url = "https://youtu.be/gvdH-gEOSB8"; break;
                case 25: url = "https://youtu.be/kdVU4FbSmu0"; break;
            }
            // implicit intent to play the song
            Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // tries to open the song in the youtube app and opens it on google if youtube app was not found
            link.setPackage("com.google.android.youtube");
            try {
                context.startActivity(link);
            } catch (ActivityNotFoundException ex) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        }
    }
}
