package com.example.aplic.dialogs;

import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aplic.R;
import com.example.aplic.activities.GameScreenActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BossLevelCompleteDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final int levelID;
    private TextView tvWorldUnlocked;
    private Button btBack;

    public BossLevelCompleteDialog(Context context, int levelID) {
        super(context);
        this.context = context;
        this.levelID = levelID;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_boss_level_complete);
        setCancelable(false);

        tvWorldUnlocked = findViewById(R.id.tvWorldUnlocked);
        tvWorldUnlocked.setVisibility(View.GONE);
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(this);
        setWorldUnlocked();
    }
    // checks if it was a first time completion and give the player a message if so
    @SuppressLint("SetTextI18n")
    public void setWorldUnlocked () {
        if (isConnectedToInternet(context)) {
            boolean check = false;
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.key);
            // checks for first time completion
            if (!user.levelComplete.get(levelID/5)) check = true;
            user.levelComplete.set(levelID/5, true);
            userRef.child("levelComplete").child(String.valueOf(levelID/5)).setValue(true);
            userRef.child("bestTime").child(String.valueOf(levelID/5)).setValue("0:00");
            //displays a message if it is
            if (check) {
                tvWorldUnlocked.setVisibility(View.VISIBLE);
                if (levelID / 5 + 1 != 6)
                    tvWorldUnlocked.setText("WORLD " + (levelID / 5 + 1) + " UNLOCKED!");
                else
                    tvWorldUnlocked.setText("LAST LEVEL COMPLETED \n CONGRATULATIONS!");
            }
        }
        else {
            tvWorldUnlocked.setVisibility(View.VISIBLE);
            tvWorldUnlocked.setText("CONNECTION LOST! \n PROGRESS NOT SAVED");
        }
    }
    // a go back button
    @Override
    public void onClick(View view) {
        if (view == btBack) {
            this.dismiss();
            GameScreenActivity tempScreen = (GameScreenActivity) context;
            tempScreen.finish();
        }
    }
}
