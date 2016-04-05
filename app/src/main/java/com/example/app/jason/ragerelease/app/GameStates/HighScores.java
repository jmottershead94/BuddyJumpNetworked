// The package location of this class.
package com.example.app.jason.ragerelease.app.GameStates;

// All of the extra includes here.
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.jason.ragerelease.R;
import com.example.app.jason.ragerelease.app.Framework.NavigationButton;
import com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase.Data;
import com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase.DataAdapter;
import com.example.app.jason.ragerelease.app.Framework.Network.Internal.SQLiteDatabase.DataDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Jason Mottershead on 09/03/2016.
 */

// High scores IS AN activity, therefore inherits from it.
// This class will display the current scores achieved by the player.
public class HighScores extends Activity
{
    // Attributes.
    private DataDatabaseHelper dataDatabaseHelper;
    private ListView list;

    // Methods.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        final Button mainMenuButton = (Button) findViewById(R.id.highScoreMainMenuButton);
        final NavigationButton button = new NavigationButton();

        list = (ListView)findViewById(R.id.list_data);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3)
            {
                deleteContactAndRefresh(view);
            }
        });

        // Initialise the database our SQLiteOpenHelper object.
        dataDatabaseHelper = new DataDatabaseHelper(this);

        populateList();

        // Go back to the main menu.
        button.isPressed(mainMenuButton, this, MainMenu.class);
    }

    private void populateList()
    {
        // Get data list from helper.
        ArrayList<Data> data = dataDatabaseHelper.getDataList();

        // Create a list adapter bound to the data list.
        DataAdapter adapter = new DataAdapter(this, data);

        // Attach the adapter to our list view.
        list.setAdapter(adapter);
    }

    private void deleteContactAndRefresh(View view)
    {
        // Get text values from child views.
        String level = ((TextView)view.findViewById(R.id.display_level_number)).getText().toString();
        String distance = ((TextView)view.findViewById(R.id.display_distance)).getText().toString();

        // Query the database.
        int result = dataDatabaseHelper.removeData(new Data(level, distance));

        // Display toast notifying user of the number of deleted rows.
        Toast.makeText(HighScores.this, result + " data was removed from the database.", Toast.LENGTH_SHORT).show();

        // Refresh the list of data.
        populateList();
    }
}
