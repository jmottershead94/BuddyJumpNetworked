// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase;

// All of the extra includes here.
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.app.jason.ragerelease.R;

import java.util.ArrayList;

/**
 * Created by Jason Mottershead on 09/03/2016.
 */

// Data adapter IS AN array adapter, therefore inherits from it.
// This class will help to place the data into a view for use.
public class DataAdapter extends ArrayAdapter<Data>
{
    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    // This will initialise our database adapter.   //
    //////////////////////////////////////////////////
    public DataAdapter(final Context context, ArrayList<Data> data)
    {
        super(context, 0, data);
    }

    //////////////////////////////////////////////////
    //                  Get View                    //
    //==============================================//
    // This will return our current view within the //
    // database and return it to place on screen.   //
    //////////////////////////////////////////////////
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the score for this position.
        Data data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_layout, parent, false);
        }

        // Obtain the text values for this piece of data.
        TextView displayLevelNumber = (TextView) convertView.findViewById(R.id.display_level_number);
        TextView displayDistance = (TextView) convertView.findViewById(R.id.display_distance);

        // Place in the level number and distance number.
        displayLevelNumber.setText(data.levelNumber);
        displayDistance.setText(data.distanceTravelled);

        // Return the completed view to render on screen.
        return convertView;
    }
}
