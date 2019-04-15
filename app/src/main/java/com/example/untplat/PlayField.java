package com.example.untplat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static com.example.untplat.MainThread.canvas;

//this class is intended to hold all of the objects which the player can interact with
public class PlayField {

    //Scenery
    private Bitmap background;
    //for future use of random obstacles\
    private List<Obstacle> debris;
    //map bounds
    private Pit thePit;
    //create objects
    private int step;
    //max obstacles that can fit on screen
    private int max;
    //should the objects be spawning
    private boolean generating;
    private int deltaT;
    private boolean gameOver;

    public PlayField() {
        //set the background
        thePit = new Pit();
        BitmapFactory bf = new BitmapFactory();
        //background = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.background);
        //background.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, true);
        //size the array on worst case scenario
        max = 5 * (Constants.SCREEN_HEIGHT/(Constants.SCREEN_WIDTH/5));
        debris = new ArrayList<>();
        step = 0;
        gameOver = false;
        generating = true;
    }

    //update
    public void update() {

        Log.d("PlayField update", "generating: " + generating);
        //update every non-collided existing block
        ListIterator<Obstacle> obstacles = debris.listIterator();
        while (obstacles.hasNext()) {
            Obstacle current = obstacles.next();
            //lower all the blocks if they have reached the upper quadrant
            if (current.getLoc().y < Constants.SCREEN_HEIGHT / 4 && current.getStatus()) {
                for (int i = 0; i <= obstacles.nextIndex(); i++)
                    debris.get(i).lowerMax(Constants.SCREEN_HEIGHT / 6);
                    generating = false;
            }
            //this makes it generate non stop
            else
                generating = true;
            //check every block for collision
            for (int i = 0; i <= obstacles.previousIndex(); i++) {
                if (!current.getStatus())
                    current.colliding(debris.get(i));
            }
            //if it's not colliding, it's falling
            if (!current.getStatus())
                current.update();
        }

        //if no blocks should be dropping
        if(!generating)
            return;

        //if you haven't reached worst case scenario (screen full of blocks)
        //and the block at the current step doesn't exist
        //to create a block you need the bounds form thePit
        int[] bounds = new int[] {thePit.getLeft(), thePit.getRight()};
        if (debris.size() < max) {
            if (debris.isEmpty())
                debris.add(new Obstacle(bounds));
                //has the last one collided? -- change to adjustable difficulty?
            else if (debris.get(debris.size() - 1).getStatus()) {
                debris.add(new Obstacle(bounds));
            }
        }
    }

    //if we're not generating what are we doing
    private void nogen(Player player) {
        switch (player.getstatus()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                gameOver = true;
                empty();
                break;
        }
    }

    //draw
    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(110,110,110));

        canvas.drawRect(0,0,Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, paint);
        
        ListIterator<Obstacle> todraw = debris.listIterator();
        while(todraw.hasNext()) {
            Obstacle current = todraw.next();
            current.draw(canvas);
        }

        thePit.draw(canvas);
    }

    //for colliding with player
    public void collision(Player player) {
        //check if the player collides with falling debris
        List<Obstacle> allDebris = new ArrayList<>();
        allDebris.addAll(debris);
        allDebris.addAll(thePit.getWalls());
        ListIterator<Obstacle> obstacles = allDebris.listIterator();
        player.collisionReset();
        while (obstacles.hasNext()) {
            Obstacle current = obstacles.next();
            player.collides(current);
        }
        //if the collision kills the player
        if(player.getstatus() == 2)
            generating = false;
        //if we're not updating, why is that
        if(generating == false)
            nogen(player);
        Log.d("Player collision", "status " + player.getstatus());
    }

    public boolean getFull() {
        return(debris.size() == max);
    }

    public boolean getGame() { return gameOver; }

    public void empty() {
        debris.clear();
    }

    public void newGame() { gameOver = false; generating = true;}
}
