// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.AndroidOSHandlers;

// All of the extra includes here.
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Jason Mottershead on 20/02/2016.
 */

// SMSHandler IS AN Activity, therefore inherits from it.
// This class will make use of the Android's own sms application.
public class SMSHandler extends Activity
{
    // Attributes.
    // Private.
    private final String standardMessage = "I have played Buddy Jump, you know, that really cool and awesome game in development by 1300455. Yeah...";

    // Methods.
    public SMSHandler(final Activity gameOverScreen)
    {
        // An intent that takes us to an sms activity.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));

        // The default message to the SMS application.
        intent.putExtra("sms_body", standardMessage);

        // This should go to the sms app.
        gameOverScreen.startActivity(intent);
    }
}
