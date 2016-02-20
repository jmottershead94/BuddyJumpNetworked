// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

import org.jbox2d.dynamics.World;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// This class will provide us with access to the most commonly used things in multiple classes.
// This also means that we won't have to pass in all of the parameters each time we want to use a resource.
// We can just have a resource pointer and gain access to common game properties.
public class Resources
{
    // Attributes.
    // Private.
    private Activity activity = null;
    private Context context = null;
    private RelativeLayout background = null;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private World world;

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up private attributes and     //
    //  getters to access common game properties.   //
    //  If there are any more common game           //
    //  properties we need access to from multiple  //
    //  classes, we can just add in "X" parameters  //
    //  here and create a nice access point for it  //
    //  from here.                                  //
    //////////////////////////////////////////////////
    public Resources(final Activity gameActivity, final Context gameContext, final RelativeLayout gameBackground, final int gameScreenWidth, final int gameScreenHeight, final World gameWorld)
    {
        activity = gameActivity;
        context = gameContext;
        background = gameBackground;
        screenWidth = gameScreenWidth;
        screenHeight = gameScreenHeight;
        world = gameWorld;
    }

    // Setters.
    // This will set the current background being used by us.
    public void setBackground(final RelativeLayout gameBackground) { background = gameBackground; }

    // Getters.
    // This will return the current game activity.
    public Activity getActivity()       { return activity; }

    // This function will return the game context.
    public Context getContext()             { return context; }

    // This function will return the current background that we are working with.
    public RelativeLayout getBackground()   { return background; }

    // This function will return the device screen width.
    public int getScreenWidth()             { return screenWidth; }

    // This function will return the device screen height.
    public int getScreenHeight()            { return screenHeight; }

    // This function will return the current Box2D world we are using.
    public World getWorld() { return world; }
}
