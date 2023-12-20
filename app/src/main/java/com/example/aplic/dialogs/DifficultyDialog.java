package com.example.aplic.dialogs;

import static com.example.aplic.Statics.user;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aplic.R;
import com.example.aplic.activities.LevelSelectActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DifficultyDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private Button btEasy, btNormal, btHard, btConfirm;
    private int diff;
    private TextView tvDisplay;

    public DifficultyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_difficulty);
        setCancelable(false);

        btEasy = findViewById(R.id.btEasy);
        btNormal = findViewById(R.id.btNormal);
        btHard = findViewById(R.id.btHard);
        btConfirm = findViewById(R.id.btConfirm);
        btEasy.setOnClickListener(this);
        btNormal.setOnClickListener(this);
        btHard.setOnClickListener(this);
        btConfirm.setOnClickListener(this);
        tvDisplay = findViewById(R.id.tvDisplay);
        diff = user.difficulty;
    }

    @Override
    public void onClick(View view) {
        // gives information to the player about each difficulty
        if (view == btEasy) {
            diff = 0;
            tvDisplay.setText("-lower obstacle movement speed \n -gain less score");
        }
        if (view == btNormal) {
            diff = 1;
            tvDisplay.setText("-normal obstacle movement \n -gain normal score");
        }
        if (view == btHard) {
            diff = 2;
            tvDisplay.setText("-higher obstacle movement speed \n -gain more score");
        }
        // saves the chosen difficulty to firebase, changes the color of the button in LevelSelectActivity
        // to the new color, closes the dialog
        if (view == btConfirm) {
            user.difficulty = diff;
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.key);
            userRef.child("difficulty").setValue(user.difficulty);
            LevelSelectActivity levelSelectActivity = (LevelSelectActivity) context;
            levelSelectActivity.difficultyColor();
            dismiss();
        }
    }
}