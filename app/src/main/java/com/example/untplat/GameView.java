package com.example.untplat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
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
    //to discern touch time
    private long touchMs, releaseMs;
    //temporary rect to make collisions
    private Rect OBJ;

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
        user = new Player(new Rect(0,0,100,100));

        //create thread
        thread = new MainThread(getHolder(), this);

        //temp object
        OBJ = new Rect( Constants.SCREEN_WIDTH/2-250,Constants.SCREEN_HEIGHT/2-250,Constants.SCREEN_WIDTH/2+250, Constants.SCREEN_HEIGHT+500);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            //on tap, begin timer
            case MotionEvent.ACTION_DOWN:
                touchMs = System.currentTimeMillis();
                break;
            //on release, jump player, set final x destination
            case MotionEvent.ACTION_UP:
                releaseMs = System.currentTimeMillis();
                user.move(x);
                if((releaseMs - touchMs) < 200)
                    user.jump();
                break;
            //on drag, move player
            case MotionEvent.ACTION_MOVE:
                user.move(x);
                break;
        }
        return true;
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
    public void update(float deltaT) {
        //update the player, keep track of time passed
        user.collides(OBJ);
        user.update(deltaT);
    }


    //draws the scenery
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        //set the scenery
        level.draw(canvas);
        //render the player
        user.draw(canvas);

        canvas.drawRect(OBJ, new Paint(Color.BLUE));
    }
}
