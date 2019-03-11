package com.example.untplat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

//this class is intended to hold all of the objects which the player can interact with
public class PlayField {

    //Scenery
    private Bitmap background;
    //for future use of random obstacles
    //private Obstacle[] debris;

    public PlayField(Point dimensions) {
        //set the background
        BitmapFactory bf = new BitmapFactory();
        background = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.background);
        background.createScaledBitmap(background, dimensions.x, dimensions.y, true);
        //for future use
        //size the array on worst case scenario
        //debris = new Obstacle[5 * (Constants.SCREEN_HEIGHT/5)];
    }

    //draw
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);
        //for future use
        //for every obstacle in array, draw rect, get paint
        //for(int i = 0; i < debris.length; i++)
        //    canvas.drawRect(debris[i].getRect(), debris[i].getPaint());
    }
}
