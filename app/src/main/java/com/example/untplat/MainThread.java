package com.example.untplat;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

//A THREAD CLASS TO BE USED FOR RUNNING SYNCHRONIZED GAME LOOP
public class MainThread extends Thread {
    //game surfaceholder
    private SurfaceHolder surfaceHolder;
    //game itself to use thread to run
    private GameView gameView;
    //start the thread
    private boolean running;
    //canvas to be drawn on
    public static Canvas canvas;

    //constructor
    public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    //start running
    public void setRunning(boolean isrunning) {
        running = isrunning;
    }

    //when running update and draw synchronously
    @Override
    public void run() {
        //keep track of frame times
        long lastUpdateTime = System.currentTimeMillis();
        long gameTime;

        while(running) {
            canvas = null;

            //calculate time since last render
            gameTime = System.currentTimeMillis();
            float deltaT = (gameTime - lastUpdateTime)/10f;

            //debug
            lastUpdateTime = gameTime;

            //cap render time
            if (deltaT > 2)
                deltaT = 2;

            //run threads on gameView
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.gameView.update(deltaT);
                    this.gameView.draw(canvas);
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
