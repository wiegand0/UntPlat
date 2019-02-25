package com.example.untplat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    //thread which will run the game
    private MainThread thread;
    //point through which phone dimensions will be fed to constants class
    private Point dimensions;
    //the level upon which the player will act
    private PlayField level;
    //the player
    private Player user;

    public GameView (Context context) {
        super(context);

        getHolder().addCallback(this);

        //pass relevant context to constants class
        Constants.CURRENT_CONTEXT = context;

        //record dimensions
        dimensions = new Point();
        dimensions.y = Constants.SCREEN_HEIGHT;
        dimensions.x = Constants.SCREEN_WIDTH;

        //create objects
        level = new PlayField(dimensions);
        user = new Player(new Rect(0,0,10,10));

        //create thread
        thread = new MainThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //on creation, start threads running
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    //when destroyed, join all threads
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    //updates the game logic
    public void update() {
        user.update();
    }

    //draws the scenery
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        //set the scenery
        level.draw(canvas);
        user.draw(canvas);

    }
}
