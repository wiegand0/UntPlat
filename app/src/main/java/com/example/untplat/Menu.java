package com.example.untplat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

//MENU OVERLAY--BUTTON WHEN IDLE
public class Menu {

    private boolean isPaused;
    private boolean reset;
    private int speed;
    private Paint menuColor;
    private List<Rect> buttons;

    public Menu() {
        isPaused = false;
        reset = false;
        speed = 0;

        buttons = new ArrayList<>();

        menuColor = new Paint();

        //Button List Items
        //0 for pause button
        buttons.add(new Rect(Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH/5,
                             0,
                             Constants.SCREEN_WIDTH,
                             Constants.SCREEN_WIDTH/5));
        //1 for reset button
        buttons.add(new Rect(0,0,Constants.SCREEN_WIDTH/5, Constants.SCREEN_WIDTH/5));
        //2 for speed slider
        buttons.add(new Rect(Constants.SCREEN_WIDTH/8,
                             Constants.SCREEN_HEIGHT/2,
                             Constants.SCREEN_WIDTH - Constants.SCREEN_WIDTH/8,
                             Constants.SCREEN_HEIGHT/2 + Constants.SCREEN_WIDTH/8));
        //3 for generating boolean
    }

    public boolean getPaused() {
        return isPaused;
    }

    public boolean getReset() { return reset; }

    public void setReset() { reset = !reset; }

    public void setPaused() { isPaused = !isPaused;}
    //draw menu
    public void drawMenu(Canvas canvas) {

        menuColor.setStyle(Paint.Style.FILL);
        menuColor.setColor(Color.rgb(255,0,0));

        //always draw the pause button
        canvas.drawRect(buttons.get(0), menuColor);

        //if it's paused draw the rest
        if(isPaused) {
            //reset button
            canvas.drawRect(buttons.get(1), menuColor);
            //speed slide
            canvas.drawRect(buttons.get(2), menuColor);
            //speed slider
            Rect temp = buttons.get(2);
            menuColor.setColor(Color.rgb(0,0,255));
            Rect slide = new Rect(temp.left + speed, temp.top, temp.left + temp.height() + speed, temp.bottom);
            canvas.drawRect(slide, menuColor);
        }
    }

    public void interact(Point location) {
        ListIterator<Rect> options = buttons.listIterator();
        //go through all the buttons that can be interacted with
        while(options.hasNext()) {
            //if the game isn't paused only the paused button needs to be checked
            if(options.nextIndex() >= 1 && !isPaused)
                return;

            Rect current = options.next();
            //if the player clicks in bound of the button commit an action
            if(location.x > current.left && location.x < current.right && location.y < current.bottom && location.y > current.top)
                interacted(options.previousIndex(), location);
        }
    }

    public void interacted(int selection, Point location) {
        Log.d("menu", "is: " + selection);
        switch (selection) {
            //pause button touched
            case 0:
                isPaused = !isPaused;
                break;
            //reset button touched
            case 1:
                reset = !reset;
                break;
            //speed slider touched
            case 2:
                setSlider(location);
                break;
            //generating button pressed
            case 3:
                break;
        }
    }

    public void setSlider(Point to) {
        Rect temp = buttons.get(2);
        if(to.x < temp.right - temp.height())
            speed = to.x - temp.left;
    }

    public float getSpeed() {
        Rect speedSlide = buttons.get(2);
        Log.d("speed" , "from: " + speed + ", " + speedSlide.left + ", " + speedSlide.right);
        Log.d("speed", "calc: " + (speed) + ", " + (speedSlide.right - speedSlide.left - speedSlide.height()));
        Log.d("speed", "is: " + ((float)speed / (speedSlide.right - speedSlide.left - speedSlide.height())));
        return ((float)speed / (speedSlide.right - speedSlide.left));
    }
}
