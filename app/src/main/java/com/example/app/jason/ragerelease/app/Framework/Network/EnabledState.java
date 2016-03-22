package com.example.app.jason.ragerelease.app.Framework.Network;

import android.os.Debug;
import android.os.Handler;
import android.util.Log;

import com.example.app.jason.ragerelease.app.Framework.Network.Bluetooth.BluetoothHandler;

/**
 * Created by Win8 on 22/03/2016.
 */
public class EnabledState
{
    // Attributes.
    private boolean enabled = false;    // The current enabled state for the connection type.

    // Methods.
    // This will constantly check the enabled status of the bluetooth handler.
    public void checkStatus(final BluetoothHandler bluetoothHandler)
    {
        // Setting up a handler and runnable to handle button text changes.
        final Handler handler = new Handler();
        handler.post(new Runnable()
        {
            // What happens after delay.
            @Override
            public void run()
            {
                // Check to see if bluetooth is already enabled.
                if(bluetoothHandler.getBluetoothAdapter().isEnabled())
                {
                    enabled = true;
                }
                else
                {
                    enabled = false;
                }

                // Do the same for WiFi.

                handler.postDelayed(this, 500);
            }
        });
    }

    // Getters.
    public boolean getEnabledState()
    {
        return enabled;
    }

}
