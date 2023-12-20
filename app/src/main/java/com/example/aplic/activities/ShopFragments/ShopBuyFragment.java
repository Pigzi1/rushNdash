package com.example.aplic.activities.ShopFragments;

import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplic.R;
import com.example.aplic.ShopItem;
import com.example.aplic.ShopItemAdapter;
import com.example.aplic.dialogs.PurchaseConfirmDialog;
import com.example.aplic.dialogs.ShopInfoDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShopBuyFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final Context context;
    private final DatabaseReference userRef;
    private String tag;
    private TextView tvCoins;
    private ListView lvShop;
    private ShopItemAdapter shopItemAdapter;

    public ShopBuyFragment(Context context) {
        this.context = context;
        this.userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.key);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tag = getTag();
        View view = inflater.inflate(R.layout.fragment_shop_buy, container, false);
        ImageButton ibBack = view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(viewBack -> requireActivity().finish());
        ImageButton ibInfo = view.findViewById(R.id.ibInfo);
        ibInfo.setOnClickListener(viewInfo -> {
            ShopInfoDialog shopInfoDialog = new ShopInfoDialog(context, tag);
            shopInfoDialog.show();
        });


        tvCoins = view.findViewById(R.id.tvCoins);
        setCoinsDisplay();
        ArrayList<ShopItem> shopItemList = new ArrayList<>();

        Log.i("TAG", "onCreateView: tag is " + tag);
        // add item to listview for each shop item
        switch (tag) {
            case "tagIcon":
                assert getArguments() != null;
                int[] iconImages = getArguments().getIntArray("icon_images");
                ShopItem icon0 = new ShopItem(context, iconImages[0], "Smiley face", 0, user.iconOwned.get(0));
                ShopItem icon1 = new ShopItem(context, iconImages[1], "Snowflake", 20, user.iconOwned.get(1));
                ShopItem icon2 = new ShopItem(context, iconImages[2], "Creeper", 50, user.iconOwned.get(2));
                ShopItem icon3 = new ShopItem(context, iconImages[3], "Checkers Board", 100, user.iconOwned.get(3));
                ShopItem icon4 = new ShopItem(context, iconImages[4], "Cat", 100, user.iconOwned.get(4));
                ShopItem icon5 = new ShopItem(context, iconImages[5], "Monster", 100, user.iconOwned.get(5));
                ShopItem icon6 = new ShopItem(context, iconImages[6], "Iron Man", 100, user.iconOwned.get(6));
                ShopItem icon7 = new ShopItem(context, iconImages[7], "Robot", 150, user.iconOwned.get(7));
                ShopItem icon8 = new ShopItem(context, iconImages[8], "EVW", 180, user.iconOwned.get(8));
                ShopItem icon9 = new ShopItem(context, iconImages[9], "Npesta", 200, user.iconOwned.get(9));
                shopItemList.add(icon0); shopItemList.add(icon1); shopItemList.add(icon2);
                shopItemList.add(icon3); shopItemList.add(icon4); shopItemList.add(icon5);
                shopItemList.add(icon6); shopItemList.add(icon7); shopItemList.add(icon8);
                shopItemList.add(icon9);
                break;
            case "tagBackground":
                assert getArguments() != null;
                int[] backgroundImages = getArguments().getIntArray("background_images");
                ShopItem background0 = new ShopItem(context, backgroundImages[0], "Empty", 0, user.backgroundOwned.get(0));
                ShopItem background1 = new ShopItem(context, backgroundImages[1], "Space", 20, user.backgroundOwned.get(1));
                    ShopItem background2 = new ShopItem(context, backgroundImages[2], "Wood", 50, user.backgroundOwned.get(2));
                ShopItem background3 = new ShopItem(context, backgroundImages[3], "Sky", 100, user.backgroundOwned.get(3));
                ShopItem background4 = new ShopItem(context, backgroundImages[4], "Stairs", 150, user.backgroundOwned.get(4));
                ShopItem background5= new ShopItem(context, backgroundImages[5], "Bricks", 180, user.backgroundOwned.get(5));
                ShopItem background6= new ShopItem(context, backgroundImages[6], "Cyber", 200, user.backgroundOwned.get(6));
                shopItemList.add(background0); shopItemList.add(background1); shopItemList.add(background2);
                shopItemList.add(background3); shopItemList.add(background4); shopItemList.add(background5);
                shopItemList.add(background6);
                break;
            case "tagBonus":
                assert getArguments() != null;
                int[] bonusImages = getArguments().getIntArray("bonus_images");
                ShopItem bonus0 = new ShopItem(context, bonusImages[0], "Question Mark", 0, user.bonusOwned.get(0));
                ShopItem bonus1 = new ShopItem(context, bonusImages[1], "Chest", 20, user.bonusOwned.get(1));
                ShopItem bonus2 = new ShopItem(context, bonusImages[2], "Strawberry", 50, user.bonusOwned.get(2));
                ShopItem bonus3 = new ShopItem(context, bonusImages[3], "Souls", 100, user.bonusOwned.get(3));
                ShopItem bonus4 = new ShopItem(context, bonusImages[4], "Star", 100, user.bonusOwned.get(4));
                ShopItem bonus5 = new ShopItem(context, bonusImages[5], "Light", 150, user.bonusOwned.get(5));
                ShopItem bonus6 = new ShopItem(context, bonusImages[6], "Delta Rune", 180, user.bonusOwned.get(6));
                ShopItem bonus7 = new ShopItem(context, bonusImages[7], "Golden Strawberry", 200, user.bonusOwned.get(7));
                shopItemList.add(bonus0);
                shopItemList.add(bonus1);
                shopItemList.add(bonus2);
                shopItemList.add(bonus3);
                shopItemList.add(bonus4);
                shopItemList.add(bonus5);
                shopItemList.add(bonus6);
                shopItemList.add(bonus7);
                break;
            case "tagPowerUp":
                assert getArguments() != null;
                int[] powerUpImages = getArguments().getIntArray("power_up_images");
                ShopItem powerUp0 = new ShopItem(context, powerUpImages[0], "None", 0,user.powerUpOwned.get(0));
                ShopItem powerUp1 = new ShopItem(context, powerUpImages[1], "Higher Speed", 250,user.powerUpOwned.get(1));
                ShopItem powerUp2 = new ShopItem(context, powerUpImages[2], "Smaller size", 250,user.powerUpOwned.get(2));
                ShopItem powerUp3 = new ShopItem(context, powerUpImages[3], "Slower Obstacles", 250,user.powerUpOwned.get(3));
                shopItemList.add(powerUp0);
                shopItemList.add(powerUp1);
                shopItemList.add(powerUp2);
                shopItemList.add(powerUp3);
                break;
        }
        shopItemAdapter = new ShopItemAdapter(context, 0, 0, shopItemList, tag);
        lvShop = view.findViewById(R.id.lvShop);
        lvShop.setAdapter(shopItemAdapter);
        lvShop.setOnItemClickListener(this);

        Log.i("TAG", "onCreateView: " + user);
        return view;
    }
    @SuppressLint("SetTextI18n")
    public void setCoinsDisplay() {
        tvCoins.setText("You have " + user.coins + " coins!");
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (isConnectedToInternet(context)) {
            ShopItem item = (ShopItem) lvShop.getItemAtPosition(i);
            ShopItem lastSelected = shopItemAdapter.getItem(i);

            // if owned switch owned item to it and switch the item with a tick
            if (lastSelected.isOwned()) {
                setSelectedView(i);
                lvShop.setAdapter(shopItemAdapter);
            }
            // if not owned open purchase dialog
            else {
                PurchaseConfirmDialog purchaseConfirmDialog = new PurchaseConfirmDialog(context, this, item, i, tag);
                purchaseConfirmDialog.show();
            }
        } else
            Toast.makeText(context, "Please connect to the internet!", Toast.LENGTH_SHORT).show();
    }
    // changed user and firebase selected item
    public void setSelectedView(int i) {
        switch (tag) {
            case "tagIcon":
                user.selectedIcon = i;
                userRef.child("selectedIcon").setValue(i);
                break;
            case "tagBackground":
                user.selectedBackground = i;
                userRef.child("selectedBackground").setValue(i);
                break;
            case "tagBonus":
                user.selectedBonus = i;
                userRef.child("selectedBonus").setValue(i);
                break;
            case "tagPowerUp":
                user.selectedPowerUp = i;
                userRef.child("selectedPowerUp").setValue(i);
                break;
        }
    }
    // change color of price to green
    public void updateTextColor(int position) {
        shopItemAdapter.updateTextColor(position);
        lvShop.setAdapter(shopItemAdapter);
    }
}
