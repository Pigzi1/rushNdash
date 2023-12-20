package com.example.aplic.activities;

import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.aplic.R;
import com.example.aplic.VibrationService;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OptionsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private DatabaseReference userRef;
    private SwitchMaterial switchSoundEffects, switchMusic, switchFPS;
    private Button btSave, btBack, btReset;
    private boolean music, soundEffects, FPS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.key);

        switchSoundEffects = findViewById(R.id.switchSoundEffects);
        switchMusic = findViewById(R.id.switchMusic);
        switchFPS = findViewById(R.id.switchFPS);
        btSave = findViewById(R.id.btSave);
        btBack = findViewById(R.id.btBack);
        btReset = findViewById(R.id.btReset);

        switchSoundEffects.setOnCheckedChangeListener(this);
        switchMusic.setOnCheckedChangeListener(this);
        switchFPS.setOnCheckedChangeListener(this);
        btSave.setOnClickListener(this);
        btBack.setOnClickListener(this);
        btReset.setOnClickListener(this);

        setSwitches();
    }
    // sets the switches to the user's selected option
    public void setSwitches() {
        switchMusic.setChecked(user.optMusic);
        switchSoundEffects.setChecked(user.optSoundEffects);
        switchFPS.setChecked(user.optFPS);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton == switchMusic) {
            music = b;
        }
        if (compoundButton == switchSoundEffects) {
            soundEffects = b;
        }
        if (compoundButton == switchFPS) {
            FPS = b;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btBack)
            finish();
        else if (isConnectedToInternet(this)) {
            if (view == btSave) {
                // saves the information from the switches
                startService(new Intent(this, VibrationService.class));
                user.optMusic = music;
                userRef.child("optMusic").setValue(music);

                user.optSoundEffects = soundEffects;
                userRef.child("optSoundEffects").setValue(soundEffects);

                user.optFPS = FPS;
                userRef.child("optFPS").setValue(FPS);

                Toast.makeText(this, "Options saved!", Toast.LENGTH_SHORT).show();
            } else if (view == btReset) {
                // makes an alert dialog to reset all information
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure");
                builder.setMessage("This will reset all of your data!");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    if (!isConnectedToInternet(OptionsActivity.this)) {
                        Toast.makeText(OptionsActivity.this, "Please check your internet and try again", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startService(new Intent(OptionsActivity.this, VibrationService.class));
                        // resets information
                        user.InitializeAllValues();
                        userRef.setValue(user);
                        dialog.dismiss();
                        finish();
                        Toast.makeText(OptionsActivity.this, "All data has been reset!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                AlertDialog ResetDialog = builder.create();
                ResetDialog.show();
            }
        }
        else
            Toast.makeText(this, "Please check your internet and try again", Toast.LENGTH_SHORT).show();
    }
}