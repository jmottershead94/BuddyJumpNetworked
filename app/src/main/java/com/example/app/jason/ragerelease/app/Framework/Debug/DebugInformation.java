// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Debug;

// All of the extra includes here.
import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// This class will contain methods which will allow debug information to be easily printed.
public class DebugInformation
{
    // Methods.
    //////////////////////////////////////////////////
    //          Display Short Toast Message         //
    //==============================================//
    // This will display a short toast message.     //
    //////////////////////////////////////////////////
    public static void displayShortToastMessage(final Activity currentActivity, final String toastMessage)
    {
        Toast.makeText(currentActivity.getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    //////////////////////////////////////////////////
    //          Display Long Toast Message          //
    //==============================================//
    // This will display a long toast message.      //
    //////////////////////////////////////////////////
    public static void displayLongToastMessage(final Activity currentActivity, final String toastMessage)
    {
        Toast.makeText(currentActivity.getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
    }
}
