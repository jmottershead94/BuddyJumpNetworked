// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase;

/**
 * Created by Jason Mottershead on 09/03/2016.
 */

// This class will be responsible for storing the data that will be used for our SQLite database.
public class Data
{
    // Attributes.
    // Public.
    public String levelNumber = "";
    public String distanceTravelled = "";

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will initialise our data values.        //
    //////////////////////////////////////////////////
    public Data(String levelNum, String distance)
    {
        // Initialising our attributes.
        levelNumber = levelNum;
        distanceTravelled = distance;
    }

}
