package com.example.untplat;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

//to be used for random obstacle generation
public class Obstacle {
    private int dimension;
    private int gravity;
    private Rect block;
    private Point location;
    private Paint paint;


    public Obstacle() {
        dimension = Constants.SCREEN_WIDTH/5;
        block = new Rect(0, 0, dimension, dimension);
        location = new Point((Constants.SCREEN_WIDTH-dimension)%(int)Math.random(), 0);
        paint = new Paint(Color.BLUE);
    }

    public boolean colliding(Point collWith) {
        if(location.x > collWith.x && location.x < (collWith.x + dimension)) {
            return true;
        } else if (location.x < collWith.x && location.x > (collWith.x - dimension)) {
            return true;
        } else {
            return false;
        }
    }

    public Point getLoc() {
        return location;
    }

    public Rect getRect() {
        return block;
    }

    public Paint getPaint() {
        return paint;
    }

    public void update() {
        //descend until colliding
    }

    public void draw() {

    }
}
