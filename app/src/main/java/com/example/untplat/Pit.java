package com.example.untplat;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

//A COLLECTION OF ALL THE WALL RELATED OBSTACLES && BACKGROUND
//CONTAINS WALL PIECES IN LIST
public class Pit {
    private List<Obstacle> leftWall;
    private List<Obstacle> rightWall;
    private Rect floor;
    private Rect dangerZone;
    private int wallHeight;
    private int wallWidth;

    public Pit() {
        leftWall = new ArrayList<>();
        rightWall = new ArrayList<>();
        wallHeight = Constants.SCREEN_HEIGHT/8;
        wallWidth = Constants.SCREEN_WIDTH/8;
        int currentHeight = Constants.SCREEN_HEIGHT - wallHeight;
        for(int i = 0; i < 8; i++) {
            Point leftLoc = new Point(0, currentHeight);
            Point rightLoc = new Point(Constants.SCREEN_WIDTH - wallWidth, currentHeight);
            leftWall.add(new Obstacle(leftLoc, wallWidth, wallHeight));
            rightWall.add(new Obstacle(rightLoc, wallWidth, wallHeight));
            currentHeight -= wallHeight;
        }

    }

    private void scroll() {

    }

    public void draw(Canvas canvas) {
        ListIterator<Obstacle> drawLeft = leftWall.listIterator();
        while(drawLeft.hasNext()) {
            Obstacle current = drawLeft.next();
            current.draw(canvas);
        }
        ListIterator<Obstacle> drawRight = rightWall.listIterator();
        while(drawRight.hasNext()) {
            Obstacle current = drawRight.next();
            current.draw(canvas);
        }
    }

    public void update() {

    }

    public List<Obstacle> getWalls() {
        List<Obstacle> walls = new ArrayList<>();
        walls.addAll(leftWall);
        walls.addAll(rightWall);
        return walls;
    }

    public int getLeft() {
        return wallWidth;
    }

    public int getRight() {
        return Constants.SCREEN_WIDTH - wallWidth;
    }
}
