// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jason Mottershead on 14/10/2015.
 */

// This class will provide easy access to other activites.
public class NavigationButton
{
    // Methods.
    //////////////////////////////////////////////////
    //                  Is Pressed                  //
    //==============================================//
    //  This will keep listening out for a button   //
    //  being pressed.                              //
    //  We can specify what button with the button  //
    //  that we pass in, as well as navigating      //
    //  from our current activity, to a new desired //
    //  activity - so long as it exists in the      //
    //  Android Manifest file.                      //
    //////////////////////////////////////////////////
    public void isPressed(final Button button, final Activity currentActivity, final Class<?> nextActivityClass)
    {
        // Set an onClickListener for the button.
        button.setOnClickListener(new View.OnClickListener()
        {
            // When the button has been clicked.
            @Override
            public void onClick(View view)
            {
                // Creating a new intent/activity.
                Intent intent = new Intent(currentActivity, nextActivityClass);

                // Starting the new activity.
                currentActivity.startActivity(intent);
            }
        });
    }
}
