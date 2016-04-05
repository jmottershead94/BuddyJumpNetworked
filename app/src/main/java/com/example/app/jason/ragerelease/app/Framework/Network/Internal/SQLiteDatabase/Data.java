// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase;

// All of the extra includes here.

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
    // Passing in the data.
    public Data(String levelNum, String distance)
    {
        levelNumber = levelNum;
        distanceTravelled = distance;
    }

}
