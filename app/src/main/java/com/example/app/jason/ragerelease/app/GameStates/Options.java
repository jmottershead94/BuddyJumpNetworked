// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// Options IS AN Activity, therefore inherits from it.
// This class will provide access to game options, and save them.
public class Options extends Activity
{
    // Attributes.
    // Private.
    private static final String PREFS_NAME = "MyPrefsFile";
    private final int duration = Toast.LENGTH_SHORT;
    private boolean playerCameraCheckedStatus = false;
    private boolean morningSky = false, afternoonSky = false, nightSky = false;
    private RelativeLayout background;
    private Switch optionOne = null;
    private RadioGroup radioGroup = null;
    private RadioButton morningButton = null;
    private RadioButton afternoonButton = null;
    private RadioButton nightButton = null;

    // Methods.
    //////////////////////////////////////////////////
    //                  On Create                   //
    //==============================================//
    //  This will set the layout and create the     //
    //  activity on the first step into the Android //
    //  lifecycle, and we shall also load in any    //
    //  previously saved options.                   //
    //////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // Setting up the button to go back to the main menu.
        final Button mainMenuButton = (Button) findViewById(R.id.mainMenuButton);
        final NavigationButton button = new NavigationButton();
        background = (RelativeLayout) findViewById(R.id.optionsBackground);
        optionOne = (Switch) findViewById(R.id.optionOne);
        radioGroup = (RadioGroup) findViewById(R.id.skyOptions);
        morningButton = (RadioButton) radioGroup.findViewById(R.id.morning);
        afternoonButton = (RadioButton) radioGroup.findViewById(R.id.afternoon);
        nightButton = (RadioButton) radioGroup.findViewById(R.id.night);

        // If there is no saved instance state.
        // There is no current state for the options menu.
        if(savedInstanceState != null)
        {
            // Get the previously assigned options values.
            playerCameraCheckedStatus = savedInstanceState.getBoolean("moptionOneCheckedStatus");
            morningSky = savedInstanceState.getBoolean("mmorningSky");
            afternoonSky = savedInstanceState.getBoolean("mafternoonSky");
            nightSky = savedInstanceState.getBoolean("mnightSky");
        }

        // Loading options for repeated use.
        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        playerCameraCheckedStatus = gameSettings.getBoolean("moptionOneCheckedStatus", optionOne.isChecked());
        morningSky = gameSettings.getBoolean("mmorningSky", morningButton.isChecked());
        afternoonSky = gameSettings.getBoolean("mafternoonSky", afternoonButton.isChecked());
        nightSky = gameSettings.getBoolean("mnightSky", nightButton.isChecked());

        // Setting each option to its' corresponding checked status.
        optionOne.setChecked(playerCameraCheckedStatus);
        morningButton.setChecked(morningSky);
        afternoonButton.setChecked(afternoonSky);
        nightButton.setChecked(nightSky);

        // Handling all of the option responses.
        OptionResponses();

        // If the main menu button has been pressed.
        // Navigate the user back to the main menu.
        button.isPressed(mainMenuButton, this, MainMenu.class);
    }

    //////////////////////////////////////////////////
    //               Option Responses               //
    //==============================================//
    //  With our access to the current option       //
    //  states, we will provide a response so that  //
    //  the user is given some feedback, and so     //
    //  that we can register the result of the      //
    //  option.                                     //
    //////////////////////////////////////////////////
    private void OptionResponses()
    {
        // Local attributes.
        final Context context = getApplicationContext();
        final CharSequence optionOneOnText = "Players will take camera images for sprites.";
        final CharSequence optionOneOffText = "Players will use sprite images.";

        // What happens when option one has been checked.
        optionOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // If the switch is on.
                if (isChecked)
                {
                    // Checked response code here...
                    // Set the options background colour to blue.
                    playerCameraCheckedStatus = true;
                    Toast.makeText(context, optionOneOnText, duration).show();
                }
                // Otherwise, the switch is off.
                else
                {
                    playerCameraCheckedStatus = false;
                    Toast.makeText(context, optionOneOffText, duration).show();
                }
            }
        });

        // What happens when an option is selected in the radio group.
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID)
            {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedID);
                boolean isChecked = checkedRadioButton.isChecked();

                if(checkedID == R.id.morning)
                {
                    morningButton.setChecked(true);
                    Toast.makeText(context, "Morning Sky Selected", duration).show();
//                    morningSky = true;
//                    afternoonSky = false;
//                    nightSky = false;
                }
                else if(checkedID == R.id.afternoon)
                {
                    afternoonButton.setChecked(true);
                    Toast.makeText(context, "Afternoon Sky Selected", duration).show();
//                    morningSky = false;
//                    afternoonSky = true;
//                    nightSky = false;
                }
                else if(checkedID == R.id.night)
                {
                    nightButton.setChecked(true);
                    Toast.makeText(context, "Night Sky Selected", duration).show();
//                    morningSky = false;
//                    afternoonSky = false;
//                    nightSky = true;
                }

                SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = gameSettings.edit();

                // Saving the sky states.
                editor.putBoolean("mmorningSky", morningButton.isChecked());
                editor.putBoolean("mafternoonSky", afternoonButton.isChecked());
                editor.putBoolean("mnightSky", nightButton.isChecked());

                editor.apply();
            }
        });
    }

    //////////////////////////////////////////////////
    //              On Save Instance State          //
    //==============================================//
    //  This will save the currently selected       //
    //  options.                                    //
    //  This is called if the phone orientation     //
    //  changes, or if for any reason the phone     //
    //  is forced out of this activity and into     //
    //  another application (i.e. like a phone      //
    //  call).                                      //
    //////////////////////////////////////////////////
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        final CharSequence saveMessage = "Options saved.";

        // Save UI changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed or restarted.
        savedInstanceState.putBoolean("moptionOneCheckedStatus", optionOne.isChecked());

        // Saving the sky states.
        savedInstanceState.putBoolean("mmorningSky", morningButton.isChecked());
        savedInstanceState.putBoolean("mafternoonSky", afternoonButton.isChecked());
        savedInstanceState.putBoolean("mnightSky", nightButton.isChecked());

        super.onSaveInstanceState(savedInstanceState);

        Toast.makeText(this, saveMessage, duration).show();
    }

    //////////////////////////////////////////////////
    //            On Restore Instance State         //
    //==============================================//
    //  This will place the current selected option //
    //  back into the local boolean attribute.      //
    //  This will be called once the application    //
    //  returns to this activity when being         //
    //  previously forced out of it. From a phone   //
    //  call or a screen rotation.                  //
    //////////////////////////////////////////////////
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Just in case the application is killed off.
        super.onRestoreInstanceState(savedInstanceState);

        playerCameraCheckedStatus = savedInstanceState.getBoolean("moptionOneCheckedStatus");
        morningSky = savedInstanceState.getBoolean("mmorningSky");
        afternoonSky = savedInstanceState.getBoolean("mafternoonSky");
        nightSky = savedInstanceState.getBoolean("mnightSky");
    }

    //////////////////////////////////////////////////
    //                  On Key Down                 //
    //==============================================//
    //  In case the user presses the back key       //
    //  instead of using the save button, this will //
    //  let the user know that their option         //
    //  selection has already been saved.           //
    //////////////////////////////////////////////////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        final CharSequence saveMessage = "Options saved.";

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Toast.makeText(this, saveMessage, duration).show();
        }

        return super.onKeyDown(keyCode, event);
    }

    //////////////////////////////////////////////////
    //                  On Pause                    //
    //==============================================//
    //  This will save the currently selected       //
    //  option states, and save it to the device    //
    //  for future reference.                       //
    //  This will be called when we are leaving     //
    //  this activity.                              //
    //  When another activity is in the foreground. //
    //////////////////////////////////////////////////
    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences gameSettings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameSettings.edit();

        // Saving the player option status.
        editor.putBoolean("moptionOneCheckedStatus", optionOne.isChecked());

        // Saving the sky states.
        editor.putBoolean("mmorningSky", morningButton.isChecked());
        editor.putBoolean("mafternoonSky", afternoonButton.isChecked());
        editor.putBoolean("mnightSky", nightButton.isChecked());

        editor.apply();
    }
}
