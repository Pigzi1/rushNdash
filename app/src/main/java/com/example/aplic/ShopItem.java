package com.example.aplic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

// class for the information for every item it the shop
public class ShopItem {
    private Bitmap bitmap;
    private final String title;
    private final int price;
    private boolean owned;

    public ShopItem(Context context, int bitmapID, String title, int price, boolean owned) {
        turnIDToBitmap(context, bitmapID);
        this.title = title;
        this.price = price;
        this.owned = owned;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public String getTitle() {
        return title;
    }
    public int getPrice() {
        return price;
    }
    public boolean isOwned() {
        return owned;
    }
    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public void turnIDToBitmap(Context context, int id) {
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), id);
    }
}
