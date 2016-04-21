// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.Debug.DebugInformation;
import com.example.app.jason.ragerelease.app.Framework.Network.Internal.AndroidOSHandlers.SMSHandler;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase.Data;
import com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase.DataDatabaseHelper;

/**
 * Created by Jason Mottershead on 21/11/2015.
 */

// GameOver IS AN Activity, therefore inherits from it.
// This class will provide a game over screen.
public class GameOver extends Activity implements View.OnClickListener
{
    // Attributes.
    // Private.
    private static final String PREFS_NAME = "MyPrefsFile";         // The shared preference file name.
    private Button mainMenuButton = null;                           // The button to access the main menu.
    private Button textAFriendButton = null;                        // The button to use the SMS service.
    private Button saveScoresButton = null;                         // The button to save our current score.
    private DataDatabaseHelper dataDatabaseHelper = null;           // Access to our SQLite database.
    private TextView levelNumberText;                               // The current level number.
    private TextView distanceText;                                  // The current distance number.
    private final Activity activityReference = this;                // Reference to this activity, to be used in the runnable below.
    private final Handler checkTextPermission = new Handler();      // To check what our response is to the message box.
    private Runnable runnable = new Runnable()                      // Providing the response to our message box reply.
    {
        //////////////////////////////////////////////////
        //                      Run                     //
        //==============================================//
        // This will check the response for our message //
        // box and will either take us to the SMS       //
        // application or just reset our message box    //
        // response values.                             //
        //////////////////////////////////////////////////
        @Override
        public void run()
        {
            // If we have accepted the message box.
            if(DebugInformation.messageReply == DebugInformation.ACCEPTED_MESSAGE)
            {
                // Reset our message values.
                DebugInformation.resetMessageValues();

                // Place our level number and distance information into a string to pass to SMS.
                String overallScore = levelNumberText.getText().toString() + "\n" + distanceText.getText().toString();

                // Go to the sms application.
                final SMSHandler smsHandler = new SMSHandler(activityReference, overallScore);

                // Reset our message values.
                DebugInformation.resetMessageValues();
            }
            // If we have declined the message box.
            else if(DebugInformation.messageReply == DebugInformation.DECLINED_MESSAGE)
            {
                // Reset our message values.
                DebugInformation.resetMessageValues();
            }

            checkTextPermission.postDelayed(this, 1000);
        }
    };

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
        setContentView(R.layout.activity_game_over);

        // Accessing shared preferences.
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final int distance = gameSettings.getInt("mplayerDistance", 0);
        final int levelNumber = gameSettings.getInt("mlevelNumber", 0);

        // Initialising our attributes.
        final NavigationButton button = new NavigationButton();
        mainMenuButton = (Button) findViewById(R.id.gameOverMainMenuButton);
        textAFriendButton = (Button) findViewById(R.id.textFriendButton);
        saveScoresButton = (Button) findViewById(R.id.saveScoresButton);
        levelNumberText = (TextView) findViewById(R.id.gameOverLevelNumberText);
        distanceText = (TextView) findViewById(R.id.gameOverDistanceText);
        dataDatabaseHelper = new DataDatabaseHelper(this);

        // Getting the final distance score for the player.
        levelNumberText.setText("Level Number: " + levelNumber);
        distanceText.setText("Final Distance: " + distance);

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(mainMenuButton, this, MainMenu.class);

        // Setting the on click listener for this button.
        textAFriendButton.setOnClickListener(this);

        // Set off our handler to check what our message response is.
        checkTextPermission.post(runnable);
    }

    //////////////////////////////////////////////////
    //                  On Click                    //
    //==============================================//
    // Here we will listen our for any clicks on    //
    // our buttons, and complete specific tasks     //
    // depending on what we need to do.             //
    //////////////////////////////////////////////////
    @Override
    public void onClick(View view)
    {
        // If we have clicked on the text a friend button.
        if(view == textAFriendButton)
        {
            // Display a message box to the user, asking them if they want to allow access to the SMS service.
            DebugInformation.displayMessageBox(this, "SMS Access", "Accept if you would like to use SMS", "Accept", "Cancel");
        }
    }

    //////////////////////////////////////////////////
    //                Add To Scores                 //
    //==============================================//
    // Here we will add in our level number and     //
    // current distance number into our SQLite      //
    // database.                                    //
    // We will also check to make sure that the     //
    // current score has not already been added in. //
    //////////////////////////////////////////////////
    public void addToScores(View view)
    {
        // Place our level number and distance into string attributes.
        String level = levelNumberText.getText().toString();
        String distance = distanceText.getText().toString();

        // Check if the score doesn't already exist to avoid duplicate entries.
        if (!dataDatabaseHelper.dataExists(new Data(level, distance)))
        {
            // Add in the new score if there isn't already the same score in the list.
            dataDatabaseHelper.addData(new Data(level, distance));

            // Check how many entries we have.
            checkNumberOfDataEntries();

            // Display a message to the user, telling them that a new score has been added.
            Toast.makeText(getApplicationContext(), "New score has been added!", Toast.LENGTH_SHORT).show();
        }
        // Otherwise, the score already exists.
        else
        {
            // Display a message to the user, telling them that their current score is a duplicate.
            Toast.makeText(getApplicationContext(), "You have a similar score already saved!", Toast.LENGTH_SHORT).show();
        }
    }

    //////////////////////////////////////////////////
    //           Check Number Of Data Entries       //
    //==============================================//
    // This function will allow us to check the     //
    // number of current entries within the our     //
    // database.                                    //
    //////////////////////////////////////////////////
    private void checkNumberOfDataEntries()
    {
        // Place the current amount of database data into an attribute.
        int dataCount = dataDatabaseHelper.getAmountOfData();

        // If there is no data.
        if (dataCount == -1)
        {
            // Do nothing for now.
        }
        // Otherwise, there is some data.
        else
        {
            // Do nothing for now.
        }
    }
}