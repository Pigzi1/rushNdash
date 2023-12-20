package com.example.aplic.activities;

import static android.content.ContentValues.TAG;

import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplic.R;
import com.example.aplic.VibrationService;
import com.example.aplic.dialogs.DifficultyDialog;
import com.example.aplic.dialogs.LevelInfoDialog;
import com.example.aplic.dialogs.ObstInfoDialog;

public class LevelSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout frameLayout;
    private TextView tvWorldDisplay;
    private Button btL1, btL2, btL3, btL4, btBoss, btObstInfo, btDifficulty;
    private ImageButton ibBack, ibBackArrow, ibForwardArrow;
    private int worldNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        frameLayout = findViewById(R.id.frameLayout);

        tvWorldDisplay = findViewById(R.id.tvWorldDisplay); ibBack = findViewById(R.id.ibBack);
        btL1 = findViewById(R.id.ibLevel1); btL2 = findViewById(R.id.ibLevel2);
        btL3 = findViewById(R.id.ibLevel3); btL4 = findViewById(R.id.ibLevel4);
        btBoss = findViewById(R.id.ibBoss); ibBackArrow = findViewById(R.id.ibBackArrow); ibForwardArrow = findViewById(R.id.ibForwardArrow);
        btObstInfo = findViewById(R.id.btObstInfo);
        btDifficulty = findViewById(R.id.btDifficulty);

        btL1.setOnClickListener(this); btL2.setOnClickListener(this); btL3.setOnClickListener(this); btL4.setOnClickListener(this);
        btBoss.setOnClickListener(this); ibBack.setOnClickListener(this); ibBackArrow.setOnClickListener(this); ibForwardArrow.setOnClickListener(this);
        btObstInfo.setOnClickListener(this);
        btDifficulty.setOnClickListener(this);

        worldNum = 1;
        for (int i = 1; i <= 4; i++)
            if(user.levelComplete.get(i))
                worldNum = i + 1;
        changeWorld();

        difficultyColor();
    }

    @Override
    public void onClick(View view) {
        if (ibBack.equals(view)) { finish();
        }
        // allows the user to change between the different worlds
        else if (ibBackArrow.equals(view)) {
            worldNum--;
            changeWorld();
        } else if (ibForwardArrow.equals(view)) {
            worldNum++;
            changeWorld();
        }
        else if (isConnectedToInternet(this)) {
            if (btObstInfo.equals(view)) {
                // shows the user information about the game's objects
                ObstInfoDialog obstInfoDialog = new ObstInfoDialog(this);
                obstInfoDialog.show();
            }
            else if (btDifficulty.equals(view)) {
                // allows the user to change the difficulty
                DifficultyDialog difficultyDialog = new DifficultyDialog(this);
                difficultyDialog.show();
            } else {
                // if the button isn't any of the previous options it will be a "level" button
                // get the level id from the button and start the level from the id
                startService(new Intent(this, VibrationService.class));
                Button tempButton = (Button) view;
                int levelID = Integer.parseInt(tempButton.getText().toString());
                LevelInfoDialog levelInfoDialog = new LevelInfoDialog(this, levelID, getIntent().getExtras());
                levelInfoDialog.show();
            }
        }
        else
            Toast.makeText(this, "Please connect to a stable internet connection", Toast.LENGTH_SHORT).show();
    }
    // changes the world number and the text on the buttons
    @SuppressLint("SetTextI18n")
    public void changeWorld() {
        tvWorldDisplay.setText("World " + worldNum);
        // world number can't be less than 1 or more than 5
        ibBackArrow.setClickable(worldNum != 1);
        ibForwardArrow.setClickable(worldNum != 5);
        // changes level button text
        btL1.setText(Integer.toString(5*worldNum-4)); btL2.setText(Integer.toString(5*worldNum-3));
        btL3.setText(Integer.toString(5*worldNum-2)); btL4.setText(Integer.toString(5*worldNum-1));
        btBoss.setText(Integer.toString(5*worldNum));
        //changes the background according to the world id
        switch (worldNum) {
            case 1: frameLayout.setBackgroundColor(Color.rgb(105,105,105)); break;
            case 2: frameLayout.setBackgroundColor(Color.rgb(80,80,80)); break;
            case 3: frameLayout.setBackgroundColor(Color.rgb(60,60,60)); break;
            case 4: frameLayout.setBackgroundColor(Color.rgb(40,40,40)); break;
            case 5: frameLayout.setBackgroundColor(Color.rgb(20,20,20)); break;
        }
    }
    // changes the difficulty button according to the user's chosen difficulty
    @SuppressLint("SetTextI18n")
    public void difficultyColor() {
       switch (user.difficulty) {
           case 0: btDifficulty.setBackgroundColor(Color.argb(50,50,255,50)); btDifficulty.setText("DIFFICULTY: EASY"); break;
           case 1: btDifficulty.setBackgroundColor(Color.argb(100,177,97,11)); btDifficulty.setText("DIFFICULTY: NORMAL"); break;
           case 2: btDifficulty.setBackgroundColor(Color.argb(60,234,11,11)); btDifficulty.setText("DIFFICULTY: HARD"); break;
       }
    }
}