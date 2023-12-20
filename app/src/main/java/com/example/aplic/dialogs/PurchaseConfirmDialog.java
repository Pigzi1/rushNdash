package com.example.aplic.dialogs;

import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplic.R;
import com.example.aplic.ShopItem;
import com.example.aplic.VibrationService;
import com.example.aplic.activities.ShopFragments.ShopBuyFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PurchaseConfirmDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private final ShopBuyFragment frag;
    private final int position;
    private final ShopItem shopItem;
    private final String tag;
    private Button btConfirm, btClose;
    private final DatabaseReference userRef;

    public PurchaseConfirmDialog(Context context, ShopBuyFragment frag,ShopItem shopItem, int position, String tag) {
        super(context);
        this.context = context;
        this.frag = frag;
        this.shopItem = shopItem;
        this.position = position;
        this.tag = tag;
        this.userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.key);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_purchase_confirm);
        setCancelable(false);

        btConfirm = findViewById(R.id.btConfirm);
        btClose = findViewById(R.id.btReturn);
        btConfirm.setOnClickListener(this);
        btClose.setOnClickListener(this);

        ImageView ivProduct = findViewById(R.id.ivProduct);
        ivProduct.setImageBitmap(shopItem.getBitmap());
        TextView tvDisplay = findViewById(R.id.tvDisplay);
        tvDisplay.setText("Buy " + shopItem.getTitle() + " for " + shopItem.getPrice() + " coins?");
    }

    @SuppressLint("NonConstantResourceId")
    public void buyItem() {
        // change the bought item to true in firebase
        switch (tag) {
            case "tagIcon":
                user.iconOwned.set(position, true);
                userRef.child("iconOwned").child(String.valueOf(position)).setValue(true);
                frag.updateTextColor(position);
                break;
            case "tagBackground":
                user.backgroundOwned.set(position, true);
                userRef.child("backgroundOwned").child(String.valueOf(position)).setValue(true);
                frag.updateTextColor(position);
                break;
            case "tagBonus":
                user.bonusOwned.set(position, true);
                userRef.child("bonusOwned").child(String.valueOf(position)).setValue(true);
                frag.updateTextColor(position);
                break;
            case "tagPowerUp":
                user.powerUpOwned.set(position, true);
                userRef.child("powerUpOwned").child(String.valueOf(position)).setValue(true);
                frag.updateTextColor(position);
                break;
        }
        // set item "owned" field to true
        shopItem.setOwned(true);
        // reduces the player's coins and displays it
        user.coins = user.coins - shopItem.getPrice();
        userRef.child("coins").setValue(user.coins);
        user.itemsOwned++;
        userRef.child("itemsOwned").setValue(user.itemsOwned);
        frag.setCoinsDisplay();
    }

    @Override
    public void onClick(View view) {
        if (view == btConfirm) {
            // checks if the player has enough coins
            if (user.coins < shopItem.getPrice())
                Toast.makeText(context, "You don't have enough coins!", Toast.LENGTH_SHORT).show();
            else {
                context.startService(new Intent(context, VibrationService.class));
                buyItem();
                Toast.makeText(context, "Item successfully purchased!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
        else if (view == btClose) {
            dismiss();
        }
    }
}
