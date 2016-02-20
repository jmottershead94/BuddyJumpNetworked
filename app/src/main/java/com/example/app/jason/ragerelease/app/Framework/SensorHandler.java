// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * Created by Jason Mottershead on 22/11/2015.
 */

// Sensor Handler USES A Sensor Event Listener, therefore implements from it.
public class SensorHandler implements SensorEventListener
{
    // Attributes.
    // Protected.
    protected Activity activity = null;
    protected SensorManager sensorManager = null;
    protected Sensor accelerometer = null;
    protected long lastUpdate = 0;
    protected float last_x, last_y, last_z;
    protected static final int SHAKE_THRESHOLD = 4000;

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up the Android sensor to work //
    //  on the current activity.                    //
    //  In our general case we are using the        //
    //  accelerometer for sensor data.              //
    //////////////////////////////////////////////////
    public SensorHandler(final Activity currentActivity, final String sensorName)
    {
        activity = currentActivity;
        sensorManager = (SensorManager) currentActivity.getSystemService(sensorName);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    //////////////////////////////////////////////////
    //                 Register Listener            //
    //==============================================//
    //  This will tell the sensor to listen out for //
    //  any sensor data.                            //
    //////////////////////////////////////////////////
    public void registerListener()
    {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //////////////////////////////////////////////////
    //                 Unregister Listener          //
    //==============================================//
    //  This will tell the sensor that we do not    //
    //  need to listen out for any sensor data.     //
    //////////////////////////////////////////////////
    public void unregisterListener()
    {
        sensorManager.unregisterListener(this);
    }

    //////////////////////////////////////////////////
    //                On Accuracy Changed           //
    //==============================================//
    //  This is here for the Sensor Event Listener  //
    //  implementation.                             //
    //////////////////////////////////////////////////
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    //////////////////////////////////////////////////
    //                On Sensor Changed             //
    //==============================================//
    //  If we have any sensor data, here is where   //
    //  we will process that.                       //
    //  In our case, we shall check to see if we    //
    //  have any accelerometer data, and provide    //
    //  our response to that.                       //
    //////////////////////////////////////////////////
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Sensor currentSensor = sensorEvent.sensor;

        // If the phone is being shaked.
        if(currentSensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            // The current x, y and z values of the accelerometer data.
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // The current time from our device.
            long curTime = System.currentTimeMillis();

            // If we have more than 100ms difference since our last update.
            if ((curTime - lastUpdate) > 100)
            {
                // Calculate the difference in time.
                long diffTime = (curTime - lastUpdate);

                // Set the new last update time.
                lastUpdate = curTime;

                // Calculate the speed of the accelerometer.
                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                // Check to see if our current accelerometer speed is greater than our desired threshold.
                if (speed > SHAKE_THRESHOLD)
                {
                    // If it is, tell the device owner to have a nice day!
                    Toast.makeText(activity, "Have a nice day!", Toast.LENGTH_SHORT).show();
                }

                // Set the previous accelerometer positions for speed calculations.
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }
}
