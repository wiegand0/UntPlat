package com.example.untplat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

//AN INDIVIDUAL OBSTACLE
public class Obstacle {
    //the only motion affecting an obstacle
    private float gravity;
    //obstacle location
    private Point location;
    //obstacle fill
    private Paint paint;
    //is the obstacle colliding and thus stationary
    private boolean collided;
    //for smoothing movement
    private float vel;
    //for randomized placement
    private Random rand;
    //lower bound
    private int maxy;
    //width and height
    private int width, height;

    //make a random obstacle
    public Obstacle(int[] bounds) {
        //size of obstacle
        width = Constants.SCREEN_WIDTH/5;
        height = Constants.SCREEN_WIDTH/5;
        //bounds[0] for leftwall [1] for rightwall
        rand = new Random();
        int temp = rand.nextInt(Constants.SCREEN_WIDTH - 2* bounds[0] - width) + bounds[0];
        location = new Point(temp, -height);
        paint = new Paint(Color.BLUE);
        maxy = Constants.SCREEN_HEIGHT - height;
        gravity = Constants.SCREEN_HEIGHT/200f;
        collided = false;
    }

    //make walls
    public Obstacle(Point loc, int width, int height) {
        this.width = width;
        this.height = height;
        location = new Point(loc.x, loc.y);
        paint = new Paint(Color.BLUE);
        gravity = Constants.SCREEN_HEIGHT/80f;
        maxy = Constants.SCREEN_HEIGHT - height;
        collided = false;
    }

    public void colliding(Obstacle collWith) {
        int collx = collWith.getLoc().x;
        int colly = collWith.getLoc().y;

        if(location.x > collx && location.x < (collx + height)) {
            if(location.y + width >= colly) {
                collided = true;
                this.setY(colly - this.height - 1);
            }
        } else if (location.x < collx && location.x > (collx - width)) {
            if(location.y + height >= colly) {
                collided = true;
                this.setY(colly - this.height - 1);
            }
        } else {
            collided = false;
        }
    }

    private float interp(float goal, float vel, float deltav) {
        float difference = goal - vel;
        if(difference > deltav)
            return vel + deltav;
        if(difference < -deltav)
            return vel - deltav;
        return goal;
    }

    public Point getLoc() {
        return location;
    }

    public Rect getRect() {
        //return the rectangle at its current position
        return new Rect (location.x, location.y, location.x + width, location.y + height);
//        return new Rect(location.x,location.y,location.x + dimension,location.y + dimension);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setY(int to) {
        location.y = to;
    }

    public int getWidth() {return width;}

    public int getHeight() {return height;}

    public float getVel() { return gravity; }

    public boolean getStatus() { return collided; }

    public void lowerMax (int by) {
        //if it's getting lowered the screen is full
        //increase lower bound
        maxy += by;
        //allow for movement
        collided = false;
        //slow speed for adjusted scrolling
        //gravity = Constants.SCREEN_HEIGHT/200f;
    }

    public void update() {
        //descend until colliding
        if(location.y < maxy && !collided) {
            vel = interp(gravity, vel, 6f);
            location.y += vel;
        }

        if(location.y > maxy) {
            location.y = maxy;
            collided = true;
        }
    }

    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(255,255,255));
        paint.setStrokeWidth(3);

        canvas.drawRect(location.x,location.y,location.x + width,location.y + height, paint);
    }
}
