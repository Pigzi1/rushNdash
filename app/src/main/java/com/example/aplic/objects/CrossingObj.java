package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.aplic.Statics;

public class CrossingObj extends RectGame{

    private final Rect rect1, rect2;
    private int movingDirection = 0;

    public CrossingObj(Context context, String color, int width, int height) {
        super(context, color, width, height);
        this.pointGame.x = this.width/2;
        this.pointGame.y = -this.height;
        changeVelocities();
        rect1 = new Rect();
        rect2 = new Rect();
    }
    // rectangle 1 is created on the object's point, rectangle 2 is created on a horizontally mirrored point
    public void setRectangles (Rect rectangle1, Rect rectangle2) {
        int l1 = this.pointGame.x-width/2;
        int t1 = this.pointGame.y-this.height/2;
        int r1 = this.pointGame.x+this.width/2;
        int b1 = this.pointGame.y+this.height/2;
        rectangle1.set(l1,t1,r1,b1);
        int revX = Statics.width - this.pointGame.x;
        int l2 = revX-this.width/2;
        int t2 = this.pointGame.y-this.height/2;
        int r2 = revX+this.width/2;
        int b2 = this.pointGame.y+this.height/2;
        rectangle2.set(l2,t2,r2,b2);
    }

    @Override
    public boolean collision(Player p) {
        return Rect.intersects(this.rect1, p.getRectangle()) || Rect.intersects(rect2, p.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        setRectangles(rect1, rect2);
        canvas.drawRect(rect1, paint);
        canvas.drawRect(rect2, paint);
    }
    // every time the objects hit the wall, "movingDirection" changes and make the object's point go in the other direction horizontally
    // the object keeps moving down vertically
    @Override
    public void update() {
        if (movingDirection == 0) {
            pointGame.velX = pointGame.speed;
            if (pointGame.x > Statics.width-this.width/2)
                movingDirection = 1;
        }
        if (movingDirection == 1) {
            pointGame.velX = -pointGame.speed;
            if (pointGame.x < this.width/2)
                movingDirection = 0;
        }
        this.moveObst();
    }
    @Override
    public void changeVelocities() {
        if (movingDirection == 0)
            pointGame.velX = pointGame.speed;
        else if (movingDirection == 1)
            pointGame.velX = -pointGame.speed;
        this.pointGame.velY= (float)pointGame.speed/2;
    }
}
