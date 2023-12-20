package com.example.aplic.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public abstract class RectGame {

    protected Context context;
    protected Bitmap bitmap;
    protected PointGame pointGame;
    protected int width, height;
    protected Rect rect;
    protected Random rnd;
    protected Paint paint;
    // for objects that are images
    public RectGame(Context context, int bitmapID, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
        Bitmap temp;
        temp = BitmapFactory.decodeResource(context.getResources(), bitmapID);
        temp = Bitmap.createScaledBitmap(temp, width, height, true);
        this.bitmap = temp;
        this.pointGame = new PointGame(0, 0);
        this.rect = new Rect();
        this.rnd = new Random();
    }
    // for objects that are colors
    public RectGame(Context context, String color, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
        this.paint = new Paint();
        this.paint.setColor(Color.parseColor(color));
        this.pointGame = new PointGame(0, 0);
        this.rect = new Rect();
        this.rnd = new Random();
    }
    // sets the rectangle with the width, height around the point
    public void setRect(Rect rectangle) {
        int l = this.pointGame.x-this.width/2;
        int t = this.pointGame.y-this.height/2;
        int r = this.pointGame.x+this.width/2;
        int b = this.pointGame.y+this.height/2;
        rectangle.set(l, t, r, b);
    }
    public Rect getRectangle() {
        return this.rect;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public PointGame getPointGame() {
        return pointGame;
    }
    // moves an object to a point out of the screen where it can't be seen
    public void kill() {
        this.pointGame.x = -500;
        this.pointGame.y = -500;
        setRect(this.rect);
    }
    // move the object by changing it's x, y values
    public void moveObst() {
        this.pointGame.x += this.pointGame.velX;
        this.pointGame.y += this.pointGame.velY;
    }
    // checks for collision between the player and the object
    public boolean collision (Player p) {
        return Rect.intersects(this.rect, p.getRectangle());
    }
    // draws the object on the game panel's canvas which the function gets as a parameter
    public abstract void draw(Canvas canvas);
    // update the object properties depending on it's purpose
    public abstract void update();
    // changes the velocity of the object depending on it's properties
    public abstract void changeVelocities();
    // changes speed of the object
    // type 0: freeze
    // type 1: slow
    // type 2: fast
    // type 3: back to previous

    public void changeSpeed(int type) {
        switch (type) {
            case 0: pointGame.speed = 0; break;
            case 1: pointGame.speed = 3; break;
            case 2: pointGame.speed = pointGame.initialSpeed * 2; break;
            case 3: pointGame.speed = pointGame.initialSpeed; break;
        }
        changeVelocities();
    }
}

