package com.example.aplic;

import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ShopItemAdapter extends ArrayAdapter<ShopItem> {
    private final Context context;
    private final List<ShopItem> objects;
    private final String tag;
    private TextView tvPrice;
    public ShopItemAdapter(Context context, int resource, int textViewResourceId, List<ShopItem> objects, String tag) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.tag = tag;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public View getView(int position, View convert, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        @SuppressLint("ViewHolder") View view  = layoutInflater.inflate(R.layout.shop_layout,parent,false);
        ImageView ivProduct = view.findViewById(R.id.ivProduct);
        if (tag.equals("tagBackground")) {
            ViewGroup.LayoutParams params = ivProduct.getLayoutParams();
            params.width = 150;
            params.height = 250;
            ivProduct.setLayoutParams(params);
        }
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvPrice = view.findViewById(R.id.tvPrice);
        ImageView ivSelected = view.findViewById(R.id.ivSelected);
        ShopItem temp = objects.get(position);

        ivProduct.setImageBitmap(temp.getBitmap());
        tvTitle.setText(String.valueOf(temp.getTitle()));
        tvPrice.setText(String.valueOf(temp.getPrice()));
        // changes price color to green if owned, add a tick to selected item
        switch (tag) {
            case "tagIcon":
                if (user.iconOwned.get(position))
                    tvPrice.setTextColor(Color.GREEN);
                if (user.selectedIcon == position)
                    ivSelected.setVisibility(View.VISIBLE);
                break;
            case "tagBackground":
                if (user.backgroundOwned.get(position))
                    tvPrice.setTextColor(Color.GREEN);
                if (user.selectedBackground == position)
                    ivSelected.setVisibility(View.VISIBLE);
                break;
            case "tagBonus":
                if (user.bonusOwned.get(position))
                    tvPrice.setTextColor(Color.GREEN);
                if (user.selectedBonus == position)
                    ivSelected.setVisibility(View.VISIBLE);
                break;
            case "tagPowerUp":
                if (user.powerUpOwned.get(position))
                    tvPrice.setTextColor(Color.GREEN);
                if (user.selectedPowerUp == position)
                    ivSelected.setVisibility(View.VISIBLE);
                break;


        }
            return view;
    }
    // updates the price text color
    public void updateTextColor(int position) {
        switch (tag) {
            case "tagIcon":
                if (user.iconOwned.get(position)) {
                    tvPrice.setTextColor(Color.GREEN);
                } break;
            case "tagBackground":
                if (user.backgroundOwned.get(position)) {
                    tvPrice.setTextColor(Color.GREEN);
                } break;
            case "tagBonus":
                if (user.bonusOwned.get(position)) {
                    tvPrice.setTextColor(Color.GREEN);
                } break;
            case "tagPowerUp":
                if (user.powerUpOwned.get(position)) {
                    tvPrice.setTextColor(Color.GREEN);
                } break;

        }
    }

}
