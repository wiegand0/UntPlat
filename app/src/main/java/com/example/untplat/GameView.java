package com.example.untplat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//CONTAINS ALL ELEMENTS OF GAME
//LISTENS FOR TOUCH
//MANAGES UPDATE AND DRAW PRIORITIES
public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    //thread which will run the game
    private MainThread thread;
    //the level upon which the player will act
    private PlayField level;
    //the player
    private Player user;
    //to discern touch time
    private long touchMs, releaseMs;
    //game status, provide access to menu
    private boolean paused;
    private Menu menu;
    //game over
    private boolean gameOver;

    public GameView (Context context) {
        super(context);

        getHolder().addCallback(this);

        //pass relevant context to constants class
        Constants.CURRENT_CONTEXT = context;

        //create objects
        level = new PlayField();
        user = new Player();

        //create thread
        thread = new MainThread(getHolder(), this);

        //create menu
        menu = new Menu();
        paused = false;

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point action = new Point((int)event.getX(), (int)event.getY());
        switch (event.getAction()) {
            //on tap, begin timer
            case MotionEvent.ACTION_DOWN:
                //if they tap the corner, pause it
                if(menu.paused(action))
                    paused = true;
                else {
                    touchMs = System.currentTimeMillis();
                    paused = false;
                }
                break;
            //on release, jump player, set final x destination
            case MotionEvent.ACTION_UP:
                if(!paused) {
                    releaseMs = System.currentTimeMillis();
                    user.move(action.x);
                    if ((releaseMs - touchMs) < 200)
                        user.jump();
                }
                break;
            //on drag, move player
            case MotionEvent.ACTION_MOVE:
                if(!paused)
                    user.move(action.x);
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
        //user.collides(OBJ);
        gameOver = level.getFull();
        if(!paused && !gameOver) {
            user.update(deltaT);
            level.collision(user);
            level.update();
        }
        //if game ends
        if(gameOver) {
            level.empty();
            paused = true;
        }

    }


    //draws the scenery
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        //set the scenery
        level.draw(canvas);

        //render the player
        user.draw(canvas);

        //if paused draw the menu, else draw the pause button
        if(paused)
            menu.drawMenu(canvas);
        else
            menu.drawRes(canvas);
    }
}
