package com.example.aplic;

import static android.content.ContentValues.TAG;
import static com.example.aplic.Statics.height;
import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;
import static com.example.aplic.Statics.width;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.example.aplic.activities.GameScreenActivity;
import com.example.aplic.dialogs.DemoGameOverDialog;
import com.example.aplic.dialogs.GameOverDialog;
import com.example.aplic.dialogs.BossLevelCompleteDialog;
import com.example.aplic.objects.BonusObj;
import com.example.aplic.objects.Player;
import com.example.aplic.objects.PointGame;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private final Context context;
    private final Random rnd;
    private final SFX sfx;
    private final Bitmap bitmapBackground;
    private int counter = 0;

    private final Player player;
    private final BonusObj bonusObj;
    private final PowerUp powerUp;
    private boolean bonusTextDraw = false;
    private String bonusString = "";
    private int bonusID;
    private final Paint scorePaint, bonusPaint;
    private int extraScore;
    private boolean isNotNormalSpeed;
    private final GameSensors gameSensors;

    private ObstacleThread obstacleThread;
    private ScoreThread scoreThread;

    private final ObstacleArrayManager obstacleArrayManager;
    private int score = 0;
    private String time;
    private final int levelID;
    private String levelType;

    private boolean gameOver = false;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        this.context = context;

        // used to get the width and height of the phone
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        System.out.println("w" + width);
        System.out.println("h" + height);
        rnd = new Random();
        sfx = new SFX(context);

        levelID = ((GameScreenActivity) context).getLevelID();
        Log.i(TAG, "GamePanel: level id " + levelID);
        if (levelID == 0) {
            levelType = "demo";
        } else if (levelID % 5 != 0) {
            levelType = "normal";
        } else {
            levelType = "boss";
            switch (levelID) {
                case 5: time = "1:16"; break;
                case 10: time = "2:14"; break;
                case 15: time = "1:44"; break;
                case 20: time = "2:17"; break;
                case 25: time = "1:25"; break;
            }
        }
        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(100);
        if (levelID == 0) {
            scorePaint.setTextSize(75);
        }
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setTypeface(ResourcesCompat.getFont(context, R.font.franklin));

        bonusPaint = new Paint();
        bonusPaint.setTextSize(100);
        bonusPaint.setTextAlign(Paint.Align.CENTER);
        bonusPaint.setTypeface(ResourcesCompat.getFont(context, R.font.berlin_sans_fb_demi_bold));
        switch (user.difficulty) {
            case 0: extraScore = 75; break;
            case 1: extraScore = 100; break;
            case 2: extraScore = 125; break;
        }

        if (levelID == 0) {
            levelType = "demo";
        } else if (levelID % 5 != 0) {
            levelType = "normal";
        } else {
            levelType = "boss";
        }
        player = new Player(context, ((GameScreenActivity) context).getBitmapID(1), 100, 100);
        bitmapBackground = BitmapFactory.decodeResource(context.getResources(),((GameScreenActivity) context).getBitmapID(2));
        bonusObj = new BonusObj(context, ((GameScreenActivity) context).getBitmapID(3),225,225);
        powerUp = new PowerUp(context, this, ((GameScreenActivity) context).findViewById(R.id.ibPowerUp));

        obstacleArrayManager =  new ObstacleArrayManager(context);

        gameSensors = new GameSensors(context);

        setFocusable(true);
    }
    public SFX getSFX() {
        return sfx;
    }
    public Player getPlayer() {
        return player;
    }
    public PowerUp getPowerUp() {
        return powerUp;
    }
    public GameSensors getGameSensors() {
        return gameSensors;
    }
    public boolean isNotNormalSpeed() {
        return isNotNormalSpeed;
    }
    public void setNotNormalSpeed(boolean notNormalSpeed) {
        isNotNormalSpeed = notNormalSpeed;
    }
    public ObstacleArrayManager getObstacleArrayManager() {
        return obstacleArrayManager;
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        // starts the thread when the game panel is created
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();

        obstacleThread = new ObstacleThread(this);
        obstacleThread.setRunning(true);
        obstacleThread.start();

        scoreThread = new ScoreThread(this);
        scoreThread.setRunning(true);
        scoreThread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // moves the player to where the user is touching
        float x = e.getX();
        float y = e.getY();

        int w = player.getWidth(); int h = player.getHeight();
        // keeps the player in the bounds of the screen
        if (x < (float)w/2) {x = (float)w/2;}
        if (x > width - (float)w/2) {x = width - (float)w/2;}
        if (y < (float)h/2) {y = (float)h/2;}
        if (y > height - (float)h/2) {y = height - (float)h/2;}
        PointGame playerPoint= player.getPointGame();
        float previousX = playerPoint.x;
        float previousY = playerPoint.y;
        if (e.getAction() != MotionEvent.ACTION_UP)
        {
                float dX = x - previousX;
                float dY = y - previousY;

                float distance = (float)Math.sqrt(Math.pow(x - playerPoint.x, 2) + Math.pow(y - playerPoint.y, 2));

                float directionX = dX / distance;
                float directionY = dY / distance;

                if (distance > 5) {
                    playerPoint.setVelX(directionX * playerPoint.getSpeed());
                    playerPoint.setVelY(directionY * playerPoint.getSpeed());
                }
                playerPoint.set((int)previousX + (int)playerPoint.getVelX(), (int)previousY + (int)playerPoint.getVelY());
        }
        return true;
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // draws the canvas background
        backgroundDrawer(canvas);
        player.draw(canvas);
        // bonus objects can only appear on normal levels
        if (levelType.equals("normal"))
            bonusObj.draw(canvas);
        // draws all objects
        obstacleArrayManager.drawAll(canvas);
        switch (levelType) {
            // tells the player to login if they are playing the demo
            case "demo":
                canvas.drawText("Login for more content!", (float) width / 2, (float) height / 2, scorePaint);
                break;
            // tells the player their score if they playing a normal level
            case "normal":
                canvas.drawText("SCORE: " + score, (float) width / 2, 150, scorePaint);
                break;
            case "boss":
            // tells the player the time left when they are playing a boss level
                canvas.drawText("TIME LEFT:", (float) width / 2, 150, scorePaint);
                canvas.drawText(time, (float) width / 2, 300, scorePaint);
                break;
        }
        // stops the game and shows the dialog according to if the level is a demo or not
        if (gameOver) {
            finishGame();
            sfx.playSound("death");
            Log.i(TAG, "draw: about to stop the thread");
            if (levelID != 0)
                new Handler(Looper.getMainLooper()).post(() -> dialogMaker("over"));
            else
                new Handler(Looper.getMainLooper()).post(() -> dialogMaker("demo"));
        }
        // displays the text that tells the player which bonus they got
        // bonusTextDraw will only be true for 2 seconds after the player gets a bonus
        if (bonusTextDraw) {
            canvas.drawText(bonusString, player.getPointGame().x, player.getPointGame().y - 125, bonusPaint);
        }
    }
    public void update() {
        // updates the game object's positions when the game is running
        if(!gameOver) {
            player.update();
            bonusObj.update();
            obstacleArrayManager.updateAll();
            // if the games detects a collision with the player the game will stop and move the player to its original position
            if (obstacleArrayManager.collisionAll(player)) {
                gameOver = true;
                Log.i(TAG, "update: collision -> game over!");
                player.getPointGame().set(width/2, height - 100);
                player.update();
            }
            // if the game detects a collision with the player it will kill the bonus object and give the player a bonus
            if (bonusObj.collision(player)) {
                createBonus();
                bonusObj.kill();
                bonusObj.setStatus(0);
            }
        }
    }
    // the 2 functions create the scrolling effect of the canvas
    public void backgroundDrawer(Canvas canvas) {
        drawBitmap(canvas,bitmapBackground,0,-counter,width,height);
        drawBitmap(canvas,bitmapBackground,0,-counter+height,width,height);
        counter = counter % height;
        counter+=5;
    }
    public static synchronized void drawBitmap(Canvas canvas, Bitmap bitmap, int x, int y, int width, int height) {
        Rect source = new Rect(0,0, bitmap.getWidth(), bitmap.getHeight());
        Rect target = new Rect(x,y,x+width, y+height);
        canvas.drawBitmap(bitmap,source,target,null);
    }
    // if the player has a power up that changes object speed, the bonus will change to extra score
    public void checkBonusIDValidity() {
        if (isNotNormalSpeed)
            bonusID = 1;
    }
    // starts the bonus
    public void createBonus() {
        bonusID = 5;
//        bonusID = rnd.nextInt(7);
        checkBonusIDValidity();
        bonusTextDraw = true;
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> bonusTextDraw = false, 2000);
        switch (bonusID) {
            // adds coins
            case 0:
                int bonusCoins = levelID + rnd.nextInt(10);
                bonusString = "+" + bonusCoins;
                bonusPaint.setColor(Color.rgb(255,215,0));
                if (isConnectedToInternet(context)) {
                    user.coins += bonusCoins;
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.key);
                    userRef.child("coins").setValue(user.coins);
                }
                else
                {
                    handler.post(() -> Toast.makeText(context, "Coins were not actually added due to connectivity issues with the internet!", Toast.LENGTH_SHORT).show());
                }
                sfx.playSound("bonus_pos");
                break;
            // adds score
            case 1:
                int bonusPoints = 500 + rnd.nextInt(501);
                bonusString =  "+" + bonusPoints;
                bonusPaint.setColor(Color.GREEN);
                score += bonusPoints;
                sfx.playSound("bonus_pos");
                break;
            // lowers score
            case 2:
                int minusPoints = -500 - rnd.nextInt(501);
                bonusString = String.valueOf(minusPoints);
                bonusPaint.setColor(Color.RED);
                score += minusPoints;
                sfx.playSound("bonus_neg");
                break;
            // temp freeze objects
            case 3:
                bonusString = "FREEZE ALL OBSTACLES";
                isNotNormalSpeed = true;
                obstacleArrayManager.changeSpeedAll(0);
                handler.postDelayed(() -> {obstacleArrayManager.changeSpeedAll(3); isNotNormalSpeed = false;}, 3000);
                bonusPaint.setColor(Color.GREEN);
                sfx.playSound("bonus_pos");
                break;
            // objects speed up
            case 4:
                bonusString = "OBSTACLES ACCELERATED";
                isNotNormalSpeed = true;
                obstacleArrayManager.changeSpeedAll(2);
                handler.postDelayed(() -> {obstacleArrayManager.changeSpeedAll(3); isNotNormalSpeed = false;}, 3000);
                bonusPaint.setColor(Color.RED);
                sfx.playSound("bonus_neg");
                break;
            // decreases player size
            case 5:
                bonusString = "SIZE DECREASE!";
                bonusPaint.setColor(Color.GREEN);
                sfx.playSound("bonus_pos");
                player.setWidth(50); player.setHeight(50);
                player.createBitmap();
                handler.postDelayed(() -> {
                    player.setWidth(100);
                    player.setHeight(100);
                    player.createBitmap();
                },5000);
                break;
            // increases player size
            case 6:
                bonusString = "SIZE INCREASE!";
                bonusPaint.setColor(Color.RED);
                sfx.playSound("bonus_neg");
                player.setWidth(200); player.setHeight(200);
                player.createBitmap();
                handler.postDelayed(() -> {
                    player.setWidth(100);
                    player.setHeight(100);
                    player.createBitmap();
                },5000);
                break;
        }
    }
    // creates and shows the dialogs
    public void dialogMaker(String dialogType) {
        switch (dialogType) {
            case "over" :
                GameOverDialog gameOverDialog = new GameOverDialog(context, score, time, levelID); gameOverDialog.show(); break;
            case "demo" :
                DemoGameOverDialog demoGameOverDialog = new DemoGameOverDialog(context); demoGameOverDialog.show(); break;
            case "complete" :
                BossLevelCompleteDialog bossLevelCompleteDialog = new BossLevelCompleteDialog(context, levelID); bossLevelCompleteDialog.show(); break;
        }
    }
    // updates the score
    public void scoreUpdater() {
        if (levelType.equals("normal")) {
            this.score += extraScore;
        }
        else if (levelType.equals("boss")) {
            time = reduceTimeByOneSecond(time);
            if (time.equals("0:00")) {
                finishGame();
                new Handler(Looper.getMainLooper()).post(() -> dialogMaker("complete"));
            }
        }
    }
    // reduces the time by a second for boss levels
    public String reduceTimeByOneSecond(String time) {
        String[] timeParts = time.split(":");
        int minutes = Integer.parseInt(timeParts[0]);
        int seconds = Integer.parseInt(timeParts[1]);
        if (seconds > 0) {
            seconds -= 1;
        } else {
            seconds = 59;
            minutes -= 1;
        }
        String secondsString = String.valueOf(seconds);
        if (seconds < 10) {
            secondsString = "0" + secondsString;
        }

        return minutes + ":" + secondsString;
    }
    // stops everything that is running when the game needs to be finished
    public void finishGame() {
        thread.setRunning(false);
        scoreThread.setRunning(false);
        obstacleThread.setRunning(false);
        gameSensors.stopSensors();
        ((GameScreenActivity) context).stopMusic();
    }
}
