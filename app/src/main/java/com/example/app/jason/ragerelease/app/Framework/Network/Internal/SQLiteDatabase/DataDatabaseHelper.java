// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase;

// All of the extra includes here.
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Jason Mottershead on 09/03/2016.
 */

// Data database helper IS AN sqlite open helper, therefore inherits from it.
// This class will help navigate the SQLite database.
public class DataDatabaseHelper extends SQLiteOpenHelper
{
    // Attributes.
    // Private.
    private static final int DATABASE_VERSION = 1;                          // The current version of our database.
    private static final String DATABASE_NAME = "BuddyJumpScoreboard";      // The name of the database.
    private static final String DATA_TABLE_NAME = "data";                   // The name of the table within our database.
    private static final String[] COLUMN_NAMES = {"Level", "Distance"};     // The names of the columns.

    // Construct create query string.
    private static final String DATA_TABLE_CREATE =                         // Used to set the database columns up.
            "CREATE TABLE " + DATA_TABLE_NAME + " (" +
                    COLUMN_NAMES[0] + " TEXT, " +
                    COLUMN_NAMES[1] + " TEXT);";

    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will initialise our database helper.   //
    //////////////////////////////////////////////////
    public DataDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //////////////////////////////////////////////////
    //                  On Create                   //
    //==============================================//
    // This will set up the database if it doesn't  //
    // already exist.                               //
    //////////////////////////////////////////////////
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        // Creates the database if it doesn't exist and adds the "BuddyJump!" score table.
        // Execute SQL query.
        database.execSQL(DATA_TABLE_CREATE);
    }

    //////////////////////////////////////////////////
    //                  On Upgrade                  //
    //==============================================//
    // Empty upgrade function in order for class    //
    // implementation to work.                      //
    //////////////////////////////////////////////////
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }

    //////////////////////////////////////////////////
    //                  Add Data                    //
    //==============================================//
    // This will add in any new data into the       //
    // database.                                    //
    //////////////////////////////////////////////////
    public void addData(Data data)
    {
        // Pack score details in ContentValues object for database insertion.
        // The first parameter is a column name, the second is a value.
        ContentValues row = new ContentValues();
        row.put(this.COLUMN_NAMES[0], data.levelNumber);
        row.put(this.COLUMN_NAMES[1], data.distanceTravelled);

        // Get writable database and insert the new row to the score table.
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(DATA_TABLE_NAME, null, row);
        database.close();
    }

    //////////////////////////////////////////////////
    //              Get Amount Of Data              //
    //==============================================//
    // This will return the amount of rows within   //
    // our database.                                //
    //////////////////////////////////////////////////
    public int getAmountOfData()
    {
        // Query the database and check the number of rows returned.
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor result = database.query(DATA_TABLE_NAME, COLUMN_NAMES, null, null, null, null, null, null);

        // Make sure the query returned a valid result before trying to change text on screen.
        if(result != null)
        {
            // Display result.
            int contactsCount = result.getCount();
            database.close();
            return contactsCount;
        }
        // Otherwise, we have no results.
        else
        {
            return -1;
        }
    }

    //////////////////////////////////////////////////
    //               Get Data List                  //
    //==============================================//
    // This will return a list of the data within   //
    // our database.                                //
    //////////////////////////////////////////////////
    public ArrayList<Data> getDataList()
    {
        // Get the readable database.
        SQLiteDatabase database = this.getReadableDatabase();

        // Get all of the data by querying the database.
        Cursor result = database.query(DATA_TABLE_NAME, COLUMN_NAMES, null, null, null, null, null, null);

        // Convert results to a list of data objects.
        ArrayList<Data> data = new ArrayList<Data>();

        // Loop through each of our results.
        for(int i = 0; i < result.getCount(); i++)
        {
            // Move the cursor to this position.
            result.moveToPosition(i);

            // Create a data object with using data from level number and distance. Add it to list.
            data.add(new Data(result.getString(0), result.getString(1)));
        }

        // Return our piece of data.
        return data;
    }

    //////////////////////////////////////////////////
    //                 Data Exists                  //
    //==============================================//
    // This will determine whether or not the       //
    // current data value already exists within the //
    // database.                                    //
    //////////////////////////////////////////////////
    public boolean dataExists(Data data)
    {
        // Check if contact exists in the local database.
        ArrayList<Data> existingData = getDataList();

        // Loop through all of our current data.
        for(int i = 0; i < existingData.size(); i++)
        {
            // Go through all existing contacts and compare the details to a given contact.
            Data dataQuery = existingData.get(i);

            // If our new piece of data is equal to a current piece of data.
            if(dataQuery.levelNumber.equals(data.levelNumber) && dataQuery.distanceTravelled.equals(data.distanceTravelled))
            {
                // If everything matches up, return true.
                return true;
            }
        }

        // If nothing matches up, return false.
        return false;
    }

    //////////////////////////////////////////////////
    //                 Remove Data                  //
    //==============================================//
    // This removes a row of data and returns the   //
    // number of affected rows.                     //
    //////////////////////////////////////////////////
    public int removeData(Data data)
    {
        // Get access to the database.
        SQLiteDatabase database = this.getWritableDatabase();

        // Ready the values for this piece of data.
        String whereClause = "Level = '" + data.levelNumber + "' AND Distance = '" + data.distanceTravelled + "'";

        // Returns the number of affected rows. 0 means no rows were deleted.
        return database.delete(DATA_TABLE_NAME, whereClause, null);
    }
}
