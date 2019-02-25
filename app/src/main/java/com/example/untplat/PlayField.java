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

    public PlayField(Point dimensions) {
        //set the background
        BitmapFactory bf = new BitmapFactory();
        background = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.background);
        background.createScaledBitmap(background, dimensions.x, dimensions.y, true);
    }

    //draw
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);
    }
}
