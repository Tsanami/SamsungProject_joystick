package com.example.samsungproject;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    boolean isRunning = false;
    SurfaceHolder surfaceHolder;
    Game game;
    long prevTime,nowTime, elapsedTime;


    public GameLoop(SurfaceHolder holder, Game surfaceView) {
        surfaceHolder = holder;
        game = surfaceView;
        prevTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        Canvas canvas;
        while (isRunning){
            if(!surfaceHolder.getSurface().isValid())
                continue;
            canvas = null;
            nowTime = System.currentTimeMillis();
            elapsedTime = nowTime - prevTime;
            if(elapsedTime > 50){
                prevTime = nowTime;
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder){
                    game.draw(canvas);
                }
                if (canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    void setRunning(boolean f){
        isRunning = f;
    }
}
