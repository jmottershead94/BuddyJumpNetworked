// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Debug;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// This class will contain methods which will allow debug information to
// to be easily printed.
public class DebugInformation
{
    // Methods.
    // This method will display a short length toast message.
    public static void displayShortToastMessage(Activity currentActivity, String toastMessage)
    {
        Toast.makeText(currentActivity.getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    // This method will display a long length toast message.
    public static void displayLongToastMessage(Activity currentActivity, String toastMessage)
    {
        Toast.makeText(currentActivity.getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
    }
}
