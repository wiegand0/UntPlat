package com.example.untplat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Player {

    //where the player is located on the screen
    private Point location;
    //vertical velocity
    private int vely;
    //player container
    private Rect rect;
    //player paint
    private Paint paint;


    public Player (Rect rect) {
        this.rect = rect;
        paint = new Paint(Color.RED);
        location = new Point(0,0);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(location.x,location.y,location.x + 100,location.y + 100, paint);
    }
    
    public void update() {
        if(location.y < Constants.SCREEN_HEIGHT + 50) {
            if(location.y == 0)
                ++location.y;
            location.y += 20;
        }
        else {
            location.y = Constants.SCREEN_HEIGHT + 50;
        }
    }

    //to be called on jump
    public void jump () {
        vely = -20;
    }
}
