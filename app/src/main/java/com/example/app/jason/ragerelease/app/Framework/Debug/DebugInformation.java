// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Debug;

// All of the extra includes here.
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by Jason Mottershead on 04/04/2016.
 */

// This class will contain methods which will allow debug information to be easily printed.
public class DebugInformation
{
    // Attributes.
    public static final int NO_MESSAGE_BOX = 13;                         // This is the result number used to show that there is no message box currently.
    public static final int ACCEPTED_MESSAGE = NO_MESSAGE_BOX + 1;      // This will tell us if the user has pressed the positive option from the message box.
    public static final int DECLINED_MESSAGE = ACCEPTED_MESSAGE + 1;    // This will tell us if the user has pressed the negative option from the message box.
    public static int messageReply = NO_MESSAGE_BOX;                    // Initialising the current value of the user reply to no message box.
    public static boolean displayOnce = true;

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

    //////////////////////////////////////////////////
    //              Display Message Box             //
    //==============================================//
    // This will display a message box to the user  //
    // asking them a question.                      //
    // We can also track the response of the reply  //
    // using the messageReply attribute.            //
    //////////////////////////////////////////////////
    public static void displayMessageBox(final Context context, final String title, final String message, final String positiveOption, final String negativeOption)
    {
        // If we still need to display the message box.
        if(displayOnce)
        {
            // Create the alert box.
            AlertDialog.Builder alertBox = new AlertDialog.Builder(context);

            // Setting the title of the message box.
            alertBox.setTitle(title);

            // Setting the message for the message box.
            alertBox.setMessage(message);

            // Setting whether or not we can cancel out from this message box or not with taps.
            alertBox.setCancelable(false);

            // This will set the position option for the message box.
            alertBox.setPositiveButton(positiveOption, new DialogInterface.OnClickListener()
            {
                //////////////////////////////////////////////////
                //                  On Click                    //
                //==============================================//
                // This will define what happens when we click  //
                // on this option.                              //
                // Here we set the value of the message reply   //
                // to the same value as an accepted message to  //
                // track user response.                         //
                //////////////////////////////////////////////////
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    // We have messageReply the message box.
                    messageReply = ACCEPTED_MESSAGE;

                    // Reset the display status for this message box.
                    displayOnce = true;

                    // Remove the message box from the UI.
                    dialogInterface.cancel();
                }
            });

            // This will set the negative option for the message box.
            alertBox.setNegativeButton(negativeOption, new DialogInterface.OnClickListener()
            {
                //////////////////////////////////////////////////
                //                  On Click                    //
                //==============================================//
                // This will define what happens when we click  //
                // on this option.                              //
                // Here we set the value of the message reply   //
                // to the same value as an declined message to  //
                // track user response.                         //
                //////////////////////////////////////////////////
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    // We have declined the message box.
                    messageReply = DECLINED_MESSAGE;

                    // Reset the display status for this message box.
                    displayOnce = true;

                    // Remove the message box from the UI.
                    dialogInterface.cancel();
                }
            });

            // Create this message box.
            AlertDialog messageBox = alertBox.create();

            // Display the message box on to the screen.
            messageBox.show();

            // We no longer need to display this message box.
            displayOnce = false;
        }
    }

    //////////////////////////////////////////////////
    //              Reset Message Values            //
    //==============================================//
    // This will reset our message box values so    //
    // that we can display it again, and read the   //
    // correct response.                            //
    //////////////////////////////////////////////////
    public static void resetMessageValues()
    {
        // Reset the message reply so that we can gauge a correct response from the user when we use another message box.
        messageReply = NO_MESSAGE_BOX;

        // Reset the display status for this message box.
        displayOnce = true;
    }
}
