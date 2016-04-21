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
    private static final String PREFS_NAME = "MyPrefsFile";
    private Button mainMenuButton = null;
    private Button textAFriendButton = null;
    private Button saveScoresButton = null;
    private DataDatabaseHelper dataDatabaseHelper = null;
    private TextView levelNumberText;
    private TextView distanceText;
    private final Activity activityReference = this;
    private final Handler checkTextPermission = new Handler();
    private Runnable runnable = new Runnable()
    {
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

        // Load in options here...
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final int distance = gameSettings.getInt("mplayerDistance", 0);
        final int levelNumber = gameSettings.getInt("mlevelNumber", 0);

        // Setting up the button to go back to the main menu.
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

    @Override
    public void onClick(View view)
    {
        // If we have clicked on the text a friend button.
        if(view == textAFriendButton)
        {
            DebugInformation.displayMessageBox(this, "SMS Access", "Accept if you would like to use SMS", "Accept", "Cancel");
        }
    }

    public void addToScores(View view)
    {
        String level = levelNumberText.getText().toString();
        String distance = distanceText.getText().toString();

        // Check if the scores doesn't already exist to avoid duplicate entries.
        if (!dataDatabaseHelper.dataExists(new Data(level, distance)))
        {
            // Add in the new score if there isn't already the same score in the list.
            dataDatabaseHelper.addData(new Data(level, distance));

            // Check how many entries we have.
            checkNumberOfDataEntries();

            // Feedback message.
            Toast.makeText(getApplicationContext(), "New score has been added!", Toast.LENGTH_SHORT).show();
        }
        // Otherwise, the score already exists.
        else
        {
            // Feedback message.
            Toast.makeText(getApplicationContext(), "You have a similar score already saved!", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkNumberOfDataEntries()
    {
        int dataCount = dataDatabaseHelper.getAmountOfData();

        // If there is no data.
        if (dataCount == -1)
        {
            //text_numberOfContacts.setText("Contacts database is empty or doesn't exist.");
        }
        // Otherwise, there is some data.
        else
        {
            // text_numberOfContacts.setText("Contacts database contains " + contactsCount + " entries.");
        }
    }

    public void viewScores(View view)
    {
        Intent intent = new Intent(this, HighScores.class);
        startActivity(intent);
    }
}