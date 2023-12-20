package com.example.aplic.activities.ShopFragments;

import static com.example.aplic.Statics.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.aplic.R;

public class InventoryFragment extends Fragment {

    public InventoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        ImageButton ibBack = view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(viewBack -> requireActivity().finish());
        LinearLayout iconLayout = view.findViewById(R.id.icon_layout);
        LinearLayout backgroundLayout = view.findViewById(R.id.background_layout);
        LinearLayout bonusLayout = view.findViewById(R.id.bonus_layout);
        LinearLayout powerUpLayout = view.findViewById(R.id.power_up_layout);

        // gets images from ShopActivity
        assert getArguments() != null;
        int[] iconImages = getArguments().getIntArray("icon_images");
        int[] backgroundImages = getArguments().getIntArray("background_images");
        int[] bonusImages = getArguments().getIntArray("bonus_images");
        int[] powerUpBonusImages = getArguments().getIntArray("power_up_images");

        // make a view for each owned item and put it in its list

        for (int i = 0; i < iconImages.length; i++) {
            if (user.iconOwned.get(i)) {
                ImageView ivIcon;
                ivIcon = new ImageView(getContext());
                ivIcon.setImageResource(iconImages[i]);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(25, 0, 25, 0);
                params.width = 150;
                params.height = 150;
                ivIcon.setLayoutParams(params);
                iconLayout.addView(ivIcon);
            }
        }
        for (int i = 0; i < backgroundImages.length; i++) {
            if (user.backgroundOwned.get(i)) {
                ImageView ivBackground;
                ivBackground = new ImageView(getContext());
                ivBackground.setImageResource(backgroundImages[i]);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(25, 0, 25, 0);
                params.width = 150;
                params.height = 250;
                ivBackground.setLayoutParams(params);
                backgroundLayout.addView(ivBackground);
            }
        }
        for (int i = 0; i < bonusImages.length; i++) {
            if (user.bonusOwned.get(i)) {
                ImageView ivBonus;
                ivBonus = new ImageView(getContext());
                ivBonus.setImageResource(bonusImages[i]);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(25, 0, 25, 0);
                params.width = 150;
                params.height = 150;
                ivBonus.setLayoutParams(params);
                bonusLayout.addView(ivBonus);
            }
        }
        for (int i = 1; i < powerUpBonusImages.length; i++) {
            if (user.powerUpOwned.get(i)) {
                ImageView ivPowerUp;
                ivPowerUp = new ImageView(getContext());
                ivPowerUp.setImageResource(powerUpBonusImages[i]);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(25, 0, 25, 0);
                params.width = 150;
                params.height = 150;
                ivPowerUp.setLayoutParams(params);
                powerUpLayout.addView(ivPowerUp);
            }
        }
        return view;
    }
}