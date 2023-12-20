package com.example.aplic.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.aplic.R;
import com.example.aplic.activities.GameScreenActivity;


public class DemoGameOverDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private Button btBack;

    // This dialog closes the game and returns to LoginActivity after the player loses the demo level

    public DemoGameOverDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_demo_game_over);
        setCancelable(false);

        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btBack) {
            this.dismiss();
            GameScreenActivity tempScreen = (GameScreenActivity) context;
            tempScreen.finish();
        }
    }
}
