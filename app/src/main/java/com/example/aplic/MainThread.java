package com.example.aplic;

import static android.content.ContentValues.TAG;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.aplic.activities.GameScreenActivity;

// This class manages the main thread of the game and controls the game panel through its SurfaceHolder.
public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    public double averageFPS;
    private final SurfaceHolder surfaceHolder;
    private final GamePanel gamePanel;
    private volatile boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    // when the thread is running, boolean "running" is true
    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;
        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                synchronized (surfaceHolder) {
                    // locks the canvas and lets the game panel draw on it
                    canvas = this.surfaceHolder.lockCanvas();
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        // unlocks the canvas and updates the display
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                if (waitTime > 0)
                    sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // calculates the average frames per second
            // displays it on the screen if the player want it to from the options menu
            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == MAX_FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
//                System.out.println(averageFPS);
                ((GameScreenActivity)gamePanel.getContext()).setFPSView(averageFPS);
            }
        }

        Log.i(TAG, "run: finish running, out of run loop");
    }
}