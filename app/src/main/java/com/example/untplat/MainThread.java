package com.example.untplat;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

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
        while(running) {
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.gameView.update();
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
