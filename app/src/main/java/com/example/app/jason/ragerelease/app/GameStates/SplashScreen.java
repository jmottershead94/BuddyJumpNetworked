// The package location for this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.app.jason.ragerelease.R;

/**
 * Created by Jason Mottershead on 14/10/2015.
 */

// SplashScreen IS AN Activity, therefore inherits from it.
// This class will provide a graphic-based splash screen.
public class SplashScreen extends Activity
{
    // Methods.
    //////////////////////////////////////////////////
    //                  On Create                   //
    //==============================================//
    //  This will set the layout and create the     //
    //  activity on the first step into the Android //
    //  lifecycle.                                  //
    //////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Setting up a handler and runnable combination for a delay on the splash screen.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            // What happens after delay.
            @Override
            public void run()
            {
                // Do something after 3s have passed.
                // Creating a new intent for the title screen.
                Intent titleScreen = new Intent(SplashScreen.this, TitleScreen.class);

                // Starting the new activity.
                startActivity(titleScreen);
            }
        }, 3000);
    }
}
