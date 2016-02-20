// The package location of this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Jason Mottershead on 17/10/2015.
 */

// ImageAdapter IS A BaseAdapter, therefore inherits from it.
public class ImageAdapter extends BaseAdapter
{
    // Attributes.
    // Private.
    private Context context;
    private int[] images;

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This set up the context for the image       //
    //  adapter.                                    //
    //////////////////////////////////////////////////
    public ImageAdapter(Context gameContext)
    {
        context = gameContext;
    }

    //////////////////////////////////////////////////
    //                  Set Images                  //
    //==============================================//
    //  This place in a selection of images of our  //
    //  choice into an image array.                 //
    //////////////////////////////////////////////////
    public void setImages(int imageSelection[])
    {
        images = new int[imageSelection.length];

        if(imageSelection.length > 0)
        {
            for (int imageSelectionIndex = 0; imageSelectionIndex < imageSelection.length; imageSelectionIndex++)
            {
                images[imageSelectionIndex] = imageSelection[imageSelectionIndex];
            }
        }
    }

    //////////////////////////////////////////////////
    //                  Get Item                    //
    //==============================================//
    //  This will return the current drawable       //
    //  image.                                      //
    //////////////////////////////////////////////////
    public Object getItem(int position)
    {
        // Error checking.
        // If the position is an index that can be accessed.
        if(position >= 0 && position < images.length)
        {
            // Return the object at that position.
            return images[position];
        }

        // Otherwise, the position is not in the array index, therefore return nothing.
        return null;
    }

    //////////////////////////////////////////////////
    //                  Get View                    //
    //==============================================//
    //  This will return the view that we want from //
    //  our image array.                            //
    //  Each image in the image array will be       //
    //  processed through this and converted into   //
    //  an image view.                              //
    //////////////////////////////////////////////////
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;

        // If it's not recycled.
        if(convertView == null)
        {
            // Initialise some attributes.
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(275, 275));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);
        }
        // Otherwise, if the image is repeated.
        else
        {
            // Just make the image with the same attributes as the previous image.
            imageView = (ImageView) convertView;
        }

        // Set the image view to the drawable.
        imageView.setImageResource(images[position]);

        // Return the image view for use in the grid view.
        return imageView;
    }

    // Getters.
    // Returns the length of the image array.
    public int getCount() { return images.length; }

    // Returns an ID for the item.
    public long getItemId(int position) { return 0; }
}