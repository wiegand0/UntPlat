package com.example.untplat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

//SETS UP UI HIDING
//STARTS GAMEVIEW INSTANCE
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Get necessary constants, fill constants variables
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //get screen width
        Constants.SCREEN_WIDTH = dm.widthPixels;
        //get screen height without action or notification bar
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            Constants.SCREEN_HEIGHT = dm.heightPixels + 2*getResources().getDimensionPixelSize(resourceId);
        }
        setContentView(new GameView(this));

    }
}
