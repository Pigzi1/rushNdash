package com.example.aplic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

// a class that vibrates the phone when called
public class VibrationService extends Service {

    private Vibrator vibrator;

    public VibrationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        VibrationEffect effect = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(effect);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
    }

}