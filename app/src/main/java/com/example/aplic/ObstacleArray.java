package com.example.aplic;

import android.content.Context;
import android.graphics.Canvas;

import com.example.aplic.objects.AlphaObj;
import com.example.aplic.objects.CrossingObj;
import com.example.aplic.objects.BounceObj;
import com.example.aplic.objects.CrossingSingleObj;
import com.example.aplic.objects.DownUpObj;
import com.example.aplic.objects.GapObj;
import com.example.aplic.objects.MovingGapObj;
import com.example.aplic.objects.MovingRectObj;
import com.example.aplic.objects.Player;
import com.example.aplic.objects.PositionSensorObj;
import com.example.aplic.objects.RectGame;
import com.example.aplic.objects.WarningObj;

public class ObstacleArray {
    private final Context context;
    private final RectGame[] obstArr;
    private int id;

    public ObstacleArray(Context context, Class<?> classType, int numOfObst) {
        this.context = context;
        setIdFromClass(classType);
        this.obstArr = new RectGame[numOfObst];
        // fills the array with different instance of the object it got as a parameter
        for (int i = 0; i < numOfObst; i++) {
            RectGame currObst = makeNewInstance();
            this.obstArr[i] = currObst;
        }
    }
    // checks if any of the objects in the array touched the player, returns yes if any of them did
    public boolean collision(Player player) {
        for (RectGame obj : obstArr)
            if (obj.collision(player))
                return true;
        return false;
    }
    // draws all objects in the array on the canvas
    public void draw(Canvas canvas) {
        for (RectGame obj: obstArr) {
            obj.draw(canvas);
        }
    }
    // updates all objects in the array
    public void update() {
        for (RectGame obj : obstArr) {
            obj.update();
        }
    }
    // changes speed to all objects in the array based on the type
    public void changeSpeed(int type) {
        for (RectGame obj : obstArr) {
            obj.changeSpeed(type);
        }
    }
    // gives an id number depending on the type of the object
    private void setIdFromClass(Class<?> classType) {
        if (classType == AlphaObj.class)
            this.id = 1;
        else if (classType == MovingRectObj.class)
            this.id = 2;
        else if (classType == GapObj.class)
            this.id = 3;
        else if (classType == WarningObj.class)
            this.id = 4;
        else if (classType == DownUpObj.class)
            this.id = 5;
        else if (classType == MovingGapObj.class)
            this.id = 6;
        else if (classType == CrossingObj.class)
            this.id = 7;
        else if (classType == CrossingSingleObj.class)
            this.id = 8;
        else if (classType == BounceObj.class)
            this.id = 9;
        else if (classType == PositionSensorObj.class)
            this.id = 10;
    }
    // makes a new instance of the object based on the id
    public RectGame makeNewInstance() {
        RectGame currObj;
        switch (this.id) {
            case 1: currObj = new AlphaObj(context, "#800000",100,100); break;
            case 2: currObj = new MovingRectObj(context, "#00FF00",100,100); break;
            case 3: currObj = new GapObj(context, "#326496",                                       250, 50); break;
            case 4: currObj = new WarningObj(context, R.drawable.warning_active, 450, 450); break;
            case 5: currObj = new DownUpObj(context, "#03A9F4", 125, 125); break;
            case 6: currObj = new MovingGapObj(context, "#FF0000", 250,50); break;
            case 7: currObj = new CrossingObj(context, "#70B596",125,125); break;
            case 8: currObj = new CrossingSingleObj(context, "#CB9813",75,75); break;
            case 9: currObj = new BounceObj(context, "#FF9632", 50,50); break;
            case 10: currObj = new PositionSensorObj(context, "#FF33FF", 400,150); break;
            default: currObj = null; break;
        }
        return currObj;
    }
}
