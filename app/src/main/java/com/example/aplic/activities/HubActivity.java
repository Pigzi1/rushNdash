package com.example.aplic.activities;

import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplic.R;
import com.example.aplic.VibrationService;
import com.google.firebase.auth.FirebaseAuth;

public class HubActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btPlay, btOptions, btShop, btSignOut, btRankings;
    private Bundle bitmapArrays;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        btPlay = findViewById(R.id.btPlay);
        btOptions = findViewById(R.id.btOptions);
        btShop = findViewById(R.id.btShop);
        btSignOut = findViewById(R.id.btSignOut);
        btRankings = findViewById(R.id.btRankings);
        btPlay.setOnClickListener(this);
        btOptions.setOnClickListener(this);
        btShop.setOnClickListener(this);
        btSignOut.setOnClickListener(this);
        btRankings.setOnClickListener(this);
        int[] iconImages, backgroundImages, bonusImages, powerUpImages;
        iconImages = new int[10];
        backgroundImages = new int[7];
        bonusImages = new int[8];
        powerUpImages = new int[4];
        String[] str = new String[]{"icon", "background", "bonus", "power_up"};
        // sets all item arrays for the game and shop
        for (String type: str) {
            int i = 1;
            int imageKey = -1;
            while (imageKey != 0) {
                imageKey = getResources().getIdentifier(type + i, "drawable", getPackageName());
                if (imageKey != 0) {
                    switch (type) {
                        case "icon":
                            iconImages[i - 1] = imageKey;
                            break;
                        case "background":
                            backgroundImages[i - 1] = imageKey;
                            break;
                        case "bonus":
                            bonusImages[i - 1] = imageKey;
                            break;
                        case "power_up":
                            powerUpImages[i] = imageKey;
                            break;
                    }
                }
                i++;
            }
        }
        bitmapArrays = new Bundle();
        bitmapArrays.putIntArray("icon_images", iconImages);
        bitmapArrays.putIntArray("background_images", backgroundImages);
        bitmapArrays.putIntArray("bonus_images", bonusImages);
        bitmapArrays.putIntArray("power_up_images", powerUpImages);

        String currUserName = user.userName;
        TextView tvUserNameDisplay = findViewById(R.id.tvUserNameDisplay);
        tvUserNameDisplay.setText("Logged in as: " + currUserName);
    }
    @Override
    public void onClick(View view) {
        startService(new Intent(this, VibrationService.class));
        Intent intent;
        if (view == btPlay) {
            intent = new Intent(view.getContext(), LevelSelectActivity.class);
            intent.putExtras(bitmapArrays);
            startActivity(intent);
        } else if (view == btOptions) {
            intent = new Intent(HubActivity.this, OptionsActivity.class);
            startActivity(intent);
        } else if (view == btShop) {
            intent = new Intent(HubActivity.this, ShopActivity.class);
            intent.putExtras(bitmapArrays);
            startActivity(intent);
        } else if (view == btSignOut) {
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(HubActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (view == btRankings) {
            if (isConnectedToInternet(this)) {
                intent = new Intent(HubActivity.this, RankingsActivity.class);
                startActivity(intent);
            }
            else
                Toast.makeText(this, "Please connect to the internet first." , Toast.LENGTH_SHORT).show();
        }
    }
}