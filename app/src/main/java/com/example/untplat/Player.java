package com.example.untplat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

//THE ENTITY CONTROLLED BY THE PLAYER
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
    //for sizing the player
    private int dimension;
    //how many jumps is the player doing
    private int jumps;
    //player image
    private Bitmap player;
    //last obstacle player collided with
    private Obstacle touching;
    private boolean touched;


    public Player () {
        jumps = 0;
        dimension = Constants.SCREEN_HEIGHT/20;
        rect = new Rect(0,0,dimension,dimension);
        paint = new Paint();
        location = new Point(((Constants.SCREEN_WIDTH/2)-dimension/2),0);
        gravity = Constants.SCREEN_HEIGHT/40f;
        target = location.x;
        vely = 0;
        velx = 0;
        Point temp = new Point(0,0);
        touching = new Obstacle(temp,0,0);
        touched = false;

        BitmapFactory bf = new BitmapFactory();
        player = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.player_idle);
        player.createScaledBitmap(player, dimension, dimension,true);
    }

    //interpolate from velocity to goal by steps deltav
    private float interp(float goal, float vel, float deltav) {
        float difference = goal - vel;
        if(difference > deltav)
            return vel + deltav;
        if(difference < -deltav)
            return vel - deltav;
        return goal;
    }

    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        //paint.setColor(Color.rgb(255,255,255));

        rect = new Rect(location.x, location.y, location.x + dimension, location.y + dimension);
        canvas.drawBitmap(player, null, rect, paint);
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


        //vertical interpolation
        //adjusts velocity toward goaly
        vely = interp(goaly, vely, 6f);
        if(vely == goaly) {
            goaly = 0;
        }
        //gravity
        //player is below bounds
        if(location.y > Constants.SCREEN_HEIGHT - dimension) {
            //scoot them back up
            location.y = Constants.SCREEN_HEIGHT - dimension;
            //restore jumps
            jumps = 0;
        }
        //player is in bounds and is not jumping
        else if(location.y < Constants.SCREEN_HEIGHT - dimension && goaly == 0){
            //location gets greater by current velocity plus gravity
            location.y += gravity/2 + vely;
        }
        //player is on ground or jumping
        else {
            location.y += vely;
        }
    }

    //to be called on jump
    public void jump () {
        if(jumps < 2) {
            goaly += -Constants.SCREEN_HEIGHT / 30f;
            jumps++;
        }
    }

    public void move(int target) {
        this.target = target;
        //set goalx to movement speed, allow player to stat adjust?
        //maybe if target is far dash? -- attach to power moves
        if(location.x < target)
            goalx = Constants.SCREEN_WIDTH/30f;
        else if(location.x > target)
            goalx = -Constants.SCREEN_WIDTH/30f;
    }

    public Point getLocation() { return location; }

    public void collides(Obstacle with) {
        //collides on what side

        Rect obj = with.getRect();

        //how far over does location.x go
        float overX = location.x - (obj.left - dimension);
        //same with location.y
        float overY = location.y - (obj.top - dimension);
        //would be width and height, but with square objects they are the same
        int combinedWidth = with.getWidth() + dimension;
        int combinedHeight = with.getHeight() + dimension;

        //are they both overlapping?
        boolean overlapx = (overX > 0 && overX < combinedWidth);
        boolean overlapy = (overY > 0 && overY < combinedHeight);



        //if they're both overlapping, you're colliding
        if(overlapx && overlapy) {
            touching = with;
            touched = true;
            jumps = 0;
            //percentage wise, easier to tell when to flip
            float percentX = overX / combinedWidth;
            float percentY = overY / combinedHeight;
            //if we're talking about right or bottom side, flip percentages
            if(percentX > .5)
                percentX = (combinedWidth - overX) / combinedWidth;
            if(percentY > .5)
                percentY = (combinedHeight - overY) / combinedHeight;
            //deal with side of least interference
            if(percentX < percentY) {
                //go left
                if(overX < combinedWidth/2) {
                    velx = 0;
                    location.x = obj.left - dimension;
                //was the percentage flipped?
                //go right
                } else {
                    velx = 0;
                    location.x = obj.right;
                }
            } else if (percentY < percentX) {
                //go up
                if(overY <= combinedHeight/2) {
                    //if it's falling, slow our player to match
                    if(!with.getStatus())
                        vely = with.getVel();
                    location.y = obj.top - dimension;
                //was the percentage flipped?
                //go down
                } else {
                    goaly = 0;
                    location.y = obj.bottom;
                }
            }
        }
        else {
            touched = false;
        }
    }

}
