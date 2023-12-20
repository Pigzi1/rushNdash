package com.example.aplic;

public class ObstacleThread extends Thread{
    private boolean running;
    private final GamePanel gamePanel;

    public ObstacleThread (GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // add an array of a randomly chosen object to the list
                gamePanel.getObstacleArrayManager().addArrToList();
                // sleep after adding the array
                int sleepTime = gamePanel.getObstacleArrayManager().getSleepFromType();
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
