package com.example.aplic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.aplic.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView ivSplash = findViewById(R.id.SplashScreenImage);
        // shows an animation when the app is launched
        Animation splashAnim = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        ivSplash.startAnimation(splashAnim);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        // waits a few milliseconds after the animation ends and changes activity to login activity
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {startActivity(intent); finish();}, 3300);
    }
}