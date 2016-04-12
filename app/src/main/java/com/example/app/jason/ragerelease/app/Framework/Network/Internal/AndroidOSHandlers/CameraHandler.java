// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework.Network.Internal.AndroidOSHandlers;

// All of the extra includes here.
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.example.app.jason.ragerelease.app.Framework.Resources;

import java.io.File;

/**
 * Created by Jason Mottershead on 21/11/2015.
 */

// Camera Handler IS AN Activity, therefore inherits from it.
// This class will make use of Android's own camera application.
public class CameraHandler extends Activity
{
    // Attributes.
    // Public.
    public boolean takenPicture = false;

    // Private.
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Resources resources = null;

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up use of the Android camera  //
    //  application.                                //
    //  So long as we have defined user             //
    //  permissions in the Android Manifest file!   //
    //////////////////////////////////////////////////
    public CameraHandler(final Activity selectionScreen)
    {
        // An intent that required an image to be captured.
        // Secure means the device is secured with a pin, password etc.
        // Apps responding to this intent must not expose personal content like photos or videos on the device.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);

        // This should go to the camera app.
        selectionScreen.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        onActivityResult(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, 0, intent);
        takenPicture = true;
    }

    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up this class to have access  //
    //  to common singlePlayerGame properties.                  //
    //////////////////////////////////////////////////
    public CameraHandler(final Resources gameResources)
    {
        resources = gameResources;
    }

    //////////////////////////////////////////////////
    //                Get Last Picture              //
    //==============================================//
    //  This will return the latest image in the    //
    //  Android gallery.                            //
    //////////////////////////////////////////////////
    public Bitmap getLastPicture()
    {
        // Find the last picture.
        String[] projection = new String[]
        {
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        };

        final Cursor cursor = resources.getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

        // Put it in the image view
        if(cursor != null)
        {
            // Move to the first element (i.e. the latest image taken) in this MediaStore.Images section - i.e. gallery.
            if (cursor.moveToFirst())
            {
                // The file location of the latest image.
                String imageLocation = cursor.getString(1);

                // The file data for the latest image.
                File imageFile = new File(imageLocation);

                // If the file exists.
                if (imageFile.exists())
                {
                    // Decode the gallery image and return it!
                    Bitmap bm = BitmapFactory.decodeFile(imageLocation);
                    return bm;
                }
            }

            // We don't have an image.
            cursor.close();
        }

        return null;
    }

}
