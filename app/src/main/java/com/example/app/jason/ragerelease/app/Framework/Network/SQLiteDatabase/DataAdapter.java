// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.SQLiteDatabase;

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
    public DataAdapter(final Context context, ArrayList<Data> data)
    {
        super(context, 0, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the contacts item for this position.
        Data data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_layout, parent, false);
        }

        // Lookup view for data population.
        TextView displayLevelNumber = (TextView) convertView.findViewById(R.id.display_level_number);
        TextView displayDistance = (TextView) convertView.findViewById(R.id.display_distance);

        // Populate the data into the template view.
        displayLevelNumber.setText(data.levelNumber);
        displayDistance.setText(data.distanceTravelled);

        // Return the completed view to render on screen.
        return convertView;
    }
}
