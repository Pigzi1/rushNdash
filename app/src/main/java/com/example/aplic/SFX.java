package com.example.aplic;


import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

//  this class plays sound effects in the game
public class SFX {
    private final SoundPool soundPool;
    private final int death, explosion, bonus_pos, bonus_neg;

    @SuppressLint("ObsoleteSdkInt")
    public SFX(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(aa)
                    .build();
        }
        else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        death = soundPool.load(context, R.raw.death,1);
        explosion = soundPool.load(context, R.raw.explosion,1);
        bonus_pos = soundPool.load(context,R.raw.bonus_pos,1);
        bonus_neg = soundPool.load(context,R.raw.bonus_neg,1);

    }

    public void playSound(String sound) {
        if (user.optSoundEffects) {
            switch (sound) {
                case "death":soundPool.play(death,1,1,0,0,1); break;
                case "explosion":soundPool.play(explosion,1,1,0,0,1); break;
                case "bonus_pos":soundPool.play(bonus_pos,1,1,0,0,1); break;
                case "bonus_neg":soundPool.play(bonus_neg,1,1,0,0,1); break;
            }
        }
    }
}
