package com.example.untplat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Player {

    //where the player is located on the screen
    private Point location;
    //velocity
    private float vely, velx;
    //velocity goals for interpolation
    private float goaly, goalx;
    //gravity
    private float gravity;
    //player container
    private Rect rect;
    //player paint
    private Paint paint;
    //horizontal target
    private int target;
    //for collision detection
    private boolean collTop;
    private boolean collBottom;
    private boolean collHorz;


    public Player (Rect rect) {
        this.rect = rect;
        paint = new Paint(Color.RED);
        location = new Point(((Constants.SCREEN_WIDTH/2)-50),0);
        gravity = Constants.SCREEN_HEIGHT/40f;
        target = location.x;
        vely = 0;
        velx = 0;
    }

    //interpolate from velocity to goal by steps deltav
    private float interp(float goal, float vel, float deltav) {
        float difference = goal - vel;        Log.d("pol", "diff: " + difference + " vel: " + vel + " goal: " + goal);
        if(difference > deltav)
            return vel + deltav;
        if(difference < -deltav)
            return vel - deltav;
        return goal;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(location.x,location.y,location.x + rect.right,location.y + rect.bottom, paint);
    }

    public void update(float deltaT) {
        //-------//horizontal//-------//
        //prevent sliding
        if(velx > 0 && goalx < 0)
            velx = 0;
        else if (velx < 0 && goalx > 0)
            velx = 0;

        //horizontal interpolation
        velx = interp(goalx, velx, 3f);

        //insurance for overshooting
        if(target > location.x && (location.x + velx) / target > 1) {
            location.x = target;
            velx = 0;
            goalx = 0;
        } else if (target < location.x && (location.x + velx) / target < 1) {
            location.x = target;
            velx = 0;
            goalx = 0;
        }

        //move location toward target
        if(location.x != target)
            location.x += velx;

        //-------//vertical//-------//
        //if colliding stop vertical movement
        if(collTop) {
            vely = 0;
            goaly = 0;
        }

        //vertical interpolation
        vely = interp(goaly, vely, 6f);
        if(vely == goaly) {
            goaly = 0;
        }
        //gravity
        //player is below bounds
        if(location.y > Constants.SCREEN_HEIGHT + 50) {
            location.y = Constants.SCREEN_HEIGHT + 50;
        }
        //player is in bounds and is not jumping or colliding
        else if(location.y < Constants.SCREEN_HEIGHT + 50 && goaly == 0 && collBottom == false){
            Log.d("moving", "g: " + Math.round(gravity*deltaT) + " v: " + Math.round(vely*deltaT));
            location.y += gravity + vely;
        }
        //player is on ground
        else {
            location.y += vely;
        }
        Log.d("pos", "( " + location.x + ", " + location.y + " )");


    }

    //to be called on jump
    public void jump () {
        goaly = -Constants.SCREEN_HEIGHT/25f;
    }

    public void move(int target) {
        this.target = target;
        if(location.x < target)
            goalx = Constants.SCREEN_WIDTH/30f;
        else if(location.x > target)
            goalx = -Constants.SCREEN_WIDTH/30f;
    }

    public void collides(Rect obj) {
        boolean intHor = !(location.x > obj.right || location.x + rect.right < obj.left);

        //detect horizontal collision, only occurs between upper and lower bounds of object
        if(intHor && location.y + rect.bottom < obj.bottom && location.y > obj.top) {
            collHorz = true;
            //detect left or right collision
            //then prevent movement in that direction
            if(location.x <= obj.left) {
                if(goalx > 0)
                    goalx = 0;
                location.x = obj.left - rect.right;
            } else if(location.x >= obj.left) {
                if(goalx < 0)
                    goalx = 0;
                location.x = obj.right;
            }
        }
        else
            collHorz = false;

        //detect vertical collision, only occurs in left and right bounds, but not when colliding horizontally
        if(location.y <= obj.bottom && location.y >= obj.top && intHor && !collHorz) {
            collTop = true;
            location.y = obj.bottom;
        }
        else
            collTop = false;
        //same idea as top collision, but with the bottom
        if(location.y + rect.bottom >= obj.top && location.y + rect.bottom <= obj.bottom && intHor && !collHorz) {
            collBottom = true;
            location.y = obj.top - rect.bottom;
        }
        else
            collBottom = false;
    }
}
