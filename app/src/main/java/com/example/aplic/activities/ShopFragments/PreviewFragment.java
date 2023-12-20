package com.example.aplic.activities.ShopFragments;

import static com.example.aplic.Statics.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aplic.R;

public class PreviewFragment extends Fragment {

    public PreviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        ImageView ivIcon = view.findViewById(R.id.ivIcon);
        ImageView ivBackground = view.findViewById(R.id.ivBackground);
        ImageView ivBonus = view.findViewById(R.id.ivBonus);
        ImageView ivPowerUp = view.findViewById(R.id.ivPowerUp);
        TextView tvNoPowerUp = view.findViewById(R.id.tvNoPowerUp);
        ImageButton ibBack = view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(viewBack -> requireActivity().finish());

        // set the image of each item to owned item
        assert getArguments() != null;
        ivIcon.setImageResource(getArguments().getIntArray("icon_images")[user.selectedIcon]);
        ivBackground.setImageResource(getArguments().getIntArray("background_images")[user.selectedBackground]);
        ivBonus.setImageResource(getArguments().getIntArray("bonus_images")[user.selectedBonus]);

        // if user doesn't have a power up selected, switch the image to text saying "none"
        if (getArguments().getIntArray("power_up_images")[user.selectedPowerUp] != 0)
            ivPowerUp.setImageResource(getArguments().getIntArray("power_up_images")[user.selectedPowerUp]);
        else {
            ivPowerUp.setVisibility(View.GONE);
            tvNoPowerUp.setVisibility(View.VISIBLE);
        }
        return view;
    }
}