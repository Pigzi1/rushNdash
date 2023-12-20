package com.example.aplic;

public class ScoreThread extends Thread{
    private boolean running = false;
    private final GamePanel gamePanel;

    public ScoreThread (GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    // updates the score every second
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);
                gamePanel.scoreUpdater();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
