package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.aplic.Statics;

public class GapObj extends RectGame {
    private final Rect rect1, rect2;

    public GapObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
        this.pointGame.x = width/2 + rnd.nextInt(Statics.width-width);
        this.pointGame.y = -height;
        this.pointGame.velX = 0;
        this.pointGame.velY = pointGame.speed;
        rect1 = new Rect();
        rect2 = new Rect();
    }

    public void setRectangles (Rect rectangle1, Rect rectangle2) {
        // puts a "fake" rectangle around the gamePoint, rectangle 1 is to the left of that rectangle,
        // rectangle 2 is to the right of that rectangle
        int l1 = 0;
        int t1 = this.pointGame.y-this.height/2;
        int r1 = this.pointGame.x-this.width/2;
        int b1 = this.pointGame.y+this.height/2;
        rectangle1.set(l1,t1,r1,b1);
        int l2 = this.pointGame.x + this.width/2;
        int t2 = this.pointGame.y-this.height/2;
        int r2 = Statics.width;
        int b2 = this.pointGame.y+this.height/2;
        rectangle2.set(l2,t2,r2,b2);
    }

    @Override
    public boolean collision (Player p) {
        return Rect.intersects(this.rect1, p.getRectangle()) || Rect.intersects(rect2, p.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        setRectangles(rect1, rect2);
        canvas.drawRect(rect1, paint);
        canvas.drawRect(rect2, paint);
    }

    @Override
    public void update() {
        this.moveObst();
    }

    @Override
    public void changeVelocities() {
        this.pointGame.velY = pointGame.speed;
    }
}
