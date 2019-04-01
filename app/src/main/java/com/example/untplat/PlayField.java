package com.example.untplat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
    boolean generating;
    private int deltaT;

    public PlayField() {
        //set the background
        thePit = new Pit();
        BitmapFactory bf = new BitmapFactory();
        background = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.background);
        background.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, true);
        //size the array on worst case scenario
        max = 5 * (Constants.SCREEN_HEIGHT/(Constants.SCREEN_WIDTH/5));
        debris = new ArrayList<>();
        step = 0;
    }

    //update
    public void update() {

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
            //check every block for collision
            for (int i = 0; i < obstacles.previousIndex(); i++) {
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


    //draw
    public void draw(Canvas canvas) {
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
        while (obstacles.hasNext()) {
            Obstacle current = obstacles.next();
            player.collides(current);
        }
        //check if player collides with walls
        /*ListIterator<Obstacle> walls = thePit.getWalls().listIterator();
        while(walls.hasNext()) {
            Obstacle current = walls.next();
            player.collides(current);
        }*/
        if(player.getLocation().y < 300)
            generating = true;
    }

    public boolean getFull() {
        return(debris.size() == max);
    }

    public void empty() {
        debris.clear();
    }
}
