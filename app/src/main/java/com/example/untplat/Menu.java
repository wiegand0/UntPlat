package com.example.untplat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

//MENU OVERLAY--BUTTON WHEN IDLE
public class Menu {
    private Rect pauseMe;
    private Rect paused;
    private int pausedDim;
    private Paint menuColor;
    private Point button;

    public Menu() {
        menuColor = new Paint();

        button = new Point(Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH/5,Constants.SCREEN_WIDTH/5);

        pauseMe = new Rect(button.x,
                0,
                Constants.SCREEN_WIDTH,
                button.y);

        paused = new Rect(Constants.SCREEN_WIDTH/3,
                Constants.SCREEN_HEIGHT/3,
                Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH/3,
                Constants.SCREEN_HEIGHT - Constants.SCREEN_HEIGHT/3);
    }

    public Rect getPauseMe() {
        return pauseMe;
    }

    public Rect getPaused() {
        return paused;
    }

    public boolean paused(Point location) {
        return(location.x > button.x && location.y < button.y);
    }

    //draw for paused
    public void drawMenu(Canvas canvas) {

        menuColor.setStyle(Paint.Style.FILL);
        menuColor.setColor(Color.rgb(255,0,0));

        canvas.drawRect(paused, menuColor);

        canvas.drawRect(pauseMe, menuColor);

    }

    //draw for resumed
    public void drawRes(Canvas canvas) {

        menuColor.setStyle(Paint.Style.FILL);
        menuColor.setColor(Color.rgb(255,200,250));
        //menuColor.setStrokeWidth(3);

        canvas.drawRect(pauseMe, menuColor);

    }
}
