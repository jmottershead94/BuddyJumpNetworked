// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.External.Bluetooth;

// All of the extra includes here.
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Jason Mottershead on 22/03/2016.
 */

public class BluetoothHandler
{
    // Attributes.
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter bluetoothAdapter;

    // Methods.
    public BluetoothHandler(Context context)
    {
        // Get our current bluetooth adapter.
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If we don't have a bluetooth adapter.
        if(bluetoothAdapter == null)
        {
            // The device does not support bluetooth.
            // "Error" message.
            Toast.makeText(context, "This device does not support bluetooth.", Toast.LENGTH_SHORT).show();
        }
    }

    public void TurnOn(final Activity currentActivity)
    {
        // If bluetooth is not already enabled.
        if(!bluetoothAdapter.isEnabled())
        {
            // Start an intent to request permission to turn on bluetooth.
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            currentActivity.startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        }
    }

    public void TurnOff()
    {
        // If bluetooth is already enabled.
        if(bluetoothAdapter.isEnabled())
        {
            // Disable it.
            bluetoothAdapter.disable();
        }
    }

    // Getters.
    public BluetoothAdapter getBluetoothAdapter()
    {
        return bluetoothAdapter;
    }
}
