package com.example.aplic.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aplic.R;

public class ObstInfoDialog extends Dialog {

    private final Context context;
    private LinearLayout obstaclesList;
    private TextView tvTitle, tvDesc;

    public ObstInfoDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_obst_info);
        setCancelable(false);

        Button btClose = findViewById(R.id.btClose);
        btClose.setOnClickListener(view -> dismiss());
        obstaclesList = findViewById(R.id.obstacles_list);
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(25, 0, 25, 0);
        params.width = 150;
        params.height = 150;
        // turn every obstacle into a view and add it to the list
        for (int i = 0; i < 12; i++) {
            ImageButton ibObstacle;
            ibObstacle = new ImageButton(context);
            if (i != 5)
                ibObstacle.setBackgroundColor(Color.parseColor(getColor(i)));
            else {
                // changes the picture of warning to it's bitmap because it doesn't use a color
                @SuppressLint("UseCompatLoadingForDrawables") Drawable originalDrawable = context.getResources().getDrawable(R.drawable.warning_active, null);
                Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 150, 150, false);
                Drawable resizedDrawable = new BitmapDrawable(context.getResources(), resizedBitmap);
                ibObstacle.setBackground(resizedDrawable);

            }
            ibObstacle.setLayoutParams(params);
            ibObstacle.setOnClickListener(view -> {
                tvTitle.setVisibility(View.VISIBLE);
                tvDesc.setVisibility(View.VISIBLE);
                int index;
                for (int j = 0; j < 12; j++) {
                    if (obstaclesList.getChildAt(j) == view) {
                        index = j;
                        setInfoTitle(index);
                        setInfoDesc(index);
                        break;
                    }
                }
            });
            obstaclesList.addView(ibObstacle);
        }
    }
    // get the color for each object
    public String getColor(int i) {
        switch (i) {
            case 0: return "#800000";
            case 1: return "#00FF00";
            case 2: return "#9600FF";
            case 3: return "#646464";
            case 4: return "#326496";
            case 6: return "#03A9F4";
            case 7: return "#FF0000";
            case 8: return "#70B596";
            case 9: return "#CB9813";
            case 10: return "#FF9632";
            case 11: return "#FF33FF";
            default: return "";
        }
    }
    // set title for each object
    public void setInfoTitle (int j) {
        switch (j) {
            case 0: tvTitle.setText(R.string.alpha); break;
            case 1: tvTitle.setText(R.string.diagonal); break;
            case 2: tvTitle.setText(R.string.vertical); break;
            case 3: tvTitle.setText(R.string.horizontal); break;
            case 4: tvTitle.setText(R.string.gap); break;
            case 5: tvTitle.setText(R.string.warning); break;
            case 6: tvTitle.setText(R.string.floor); break;
            case 7: tvTitle.setText(R.string.moving_gap); break;
            case 8: tvTitle.setText(R.string.cross); break;
            case 9: tvTitle.setText(R.string.wall); break;
            case 10: tvTitle.setText(R.string.wall_floor); break;
            case 11: tvTitle.setText(R.string.sensor); break;
        }
    }
    // set description for each object
    public void setInfoDesc (int j) {
        String desc;
        switch (j) {
            case 0: desc = "This obstacle will become invisible if you move, but will turn visible again if you stand still."; break;
            case 1: desc = "This obstacle will move diagonally from the top."; break;
            case 2: desc = "This obstacle will move vertically from the top."; break;
            case 3: desc = "This obstacle will move horizontally from either sides."; break;
            case 4: desc = "This obstacles will create 2 object with a gap between them that you will need to go through."; break;
            case 5: desc = "This obstacle will give you a short warning before summoning a square that will hit you if you stand on it."; break;
            case 6: desc = "This obstacle will move down vertically and then bounce back up when they hit the bottom"; break;
            case 7: desc = "This obstacle will have 2 object moving vertically down, and the gap between them will move from side to side."; break;
            case 8: desc = "This obstacle is 2 objects that move together and cross each other."; break;
            case 9: desc = "This obstacle is an object that goes diagonally from top to bottom and bounces on the walls when it hits them."; break;
            case 10: desc = "This obstacle is an object that goes diagonally from top to bottom and bounces on the walls and the bottom floor when it hits them."; break;
            case 11: desc = "This obstacle will move based on the phone's position."; break;
            default: desc = ""; break;
        }
        tvDesc.setText(desc);
    }
}
