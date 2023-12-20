package com.example.aplic.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.aplic.R;

public class ShopInfoDialog extends Dialog {

    private final String type;

    public ShopInfoDialog(@NonNull Context context, String type) {
        super(context);
        this.type = type;
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shop_info);
        setCancelable(false);

        Button btClose = findViewById(R.id.btClose);
        btClose.setOnClickListener(view -> dismiss());
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvInfo = findViewById(R.id.tvInfo);
        // set the text to give information to the player about the properties of the item that they are buying
        switch (type) {
            case "tagIcon": tvTitle.setText(R.string.icon); tvInfo.setText("Customize the way your character looks!"); break;
            case "tagBackground": tvTitle.setText(R.string.background); tvInfo.setText("Customize the way the background looks!"); break;
            case "tagBonus": tvTitle.setText(R.string.bonus); tvInfo.setText("A bonus block is an item that you can pick up occasionally during a level, " +
                    "up a bonus can either make the level easier or harder, so be careful if you decide to pick it up \n !Customize the way your bonus block looks!"); break;
            case "tagPowerUp": tvTitle.setText(R.string.power_up); tvInfo.setText("Power ups can help you during a level and can be reactivated " +
                    " when their cool down is over! \n Purchase a power up!"); break;
        }

    }
}
