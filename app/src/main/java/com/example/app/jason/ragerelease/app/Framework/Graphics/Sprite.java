// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Graphics;

// All of the extra includes here.
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.view.View;

import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.R;

/**
 * Created by Jason Mottershead on 14/06/2015.
 */

// Sprite IS A View, therefore inherits from it.
// This class will provide a custom view for all types of singlePlayerGame objects in our singlePlayerGame.
public class Sprite extends View
{
    // Attributes.
    // Protected.
    protected boolean usingCameraImage = false;
    protected boolean remove = false;
    protected float angle = 0.0f;
    protected Bitmap sprite, image;
    protected Paint colour = null;
    protected Vector2 position = null;
    protected Vector2 dimension = null;
    protected Vector2 textureCoordinates = null;
    protected Vector2 textureDimensions = null;

    // Private.
    private Rect test, destRect;
    private final String TAG = "TKT";

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will set up the rectangles for the     //
    //  sprite canvas drawing.                      //
    //////////////////////////////////////////////////
    public Sprite(Context context)
    {
        super(context);
        test = new Rect();
        destRect = new Rect();
    }

    //////////////////////////////////////////////////
    //                      Init                    //
    //==============================================//
    //  Sets up the position (x and y) of the       //
    //  sprite and the dimensions                   //
    //  (width and height).                         //
    //  Also sets the rotation of the sprite.       //
    //////////////////////////////////////////////////
    public void init(Vector2 positions, Vector2 dimensions, float rotation)
    {
        position = new Vector2(positions.getX(), positions.getY());
        dimension = new Vector2(dimensions.getX(), dimensions.getY());
        setAngle(rotation);
    }

    //////////////////////////////////////////////////
    //                  Set Position                //
    //==============================================//
    //  Sets the vector 2 for the position.         //
    //////////////////////////////////////////////////
    public void setPosition(float x, float y)
    {
        position.set(x, y);
        postInvalidate();
    }

    //////////////////////////////////////////////////
    //               Set Dimension                  //
    //==============================================//
    //  Sets the vector 2 for the dimension.        //
    //////////////////////////////////////////////////
    public void setDimensions(float width, float height)
    {
        dimension.set(width, height);
        postInvalidate();
    }

    //////////////////////////////////////////////////
    //                  Set Angle                   //
    //==============================================//
    //  Sets the float value for the sprite         //
    //  rotation.                                    //
    //////////////////////////////////////////////////
    public void setAngle(float rotation)
    {
        angle = rotation;
        postInvalidate();
    }

    //////////////////////////////////////////////////
    //                  Set Colour                  //
    //==============================================//
    //  Sets the int value for the colour of the    //
    //  sprite.                                     //
    //////////////////////////////////////////////////
    public void setColour(int alpha, int red, int green, int blue)
    {
        colour = new Paint();
        colour.setARGB(alpha, red, green, blue);
        colour.setStyle(Paint.Style.FILL);
        postInvalidate();
    }

    //////////////////////////////////////////////////
    //              Load Camera Image               //
    //==============================================//
    //  This will load in the currently selected    //
    //  camera image, and resize the image from the //
    //  gallery so we are not trying to fit a       //
    //  480 x 720 image into a sprite.              //
    //////////////////////////////////////////////////
    public void loadCameraImage(final Bitmap cameraImage)
    {
        usingCameraImage = true;
        image = cameraImage;
        sprite = getResizedBitmap(cameraImage, (image.getWidth() / 8), (image.getHeight() / 8));
    }

    //////////////////////////////////////////////////
    //              Set Camera Image                //
    //==============================================//
    //  This will actually set the camera image     //
    //  that we want into the sprite for us.        //
    //  Use this function whenever we are using     //
    //  camera gallery images!                      //
    //////////////////////////////////////////////////
    public void setCameraImage()
    {
        float imageUConversion = image.getWidth();
        float imageVConversion = image.getHeight();

        // The current texture coordinates of the camera image (top left in this case).
        textureCoordinates = new Vector2(0.0f, 0.0f);

        // Setting the width and height of the camera image sprite.
        textureDimensions = new Vector2((1.0f * imageUConversion), (1.0f * imageVConversion));
    }

    //////////////////////////////////////////////////
    //              Get Resized Bitmap              //
    //==============================================//
    //  This will reduce the size of a bitmap image //
    //  that we pass to it, with a desired width    //
    //  and height.                                 //
    //  This is used to make sure that the singlePlayerGame     //
    //  does not lag when using camera image        //
    //  sprites.                                    //
    //////////////////////////////////////////////////
    public Bitmap getResizedBitmap(Bitmap bitmapImage, int newWidth, int newHeight)
    {
        int width = bitmapImage.getWidth();
        int height = bitmapImage.getHeight();
        float scaleWidth = (float)newWidth / (float)width;
        float scaleHeight = (float)newHeight / (float)height;

        // Creating a matrix for image manipulation.
        Matrix matrix = new Matrix();

        // Scaling the bitmap.
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreating the new scaled down bitmap image.
        Bitmap resizedBitmapImage = Bitmap.createBitmap(bitmapImage, 0, 0, width, height, matrix, false);

        return resizedBitmapImage;
    }

    //////////////////////////////////////////////////
    //                  Load Texture                //
    //==============================================//
    //  This will load the current resource file    //
    //  that we have passed in, as a texture for    //
    //  the sprite.                                 //
    //////////////////////////////////////////////////
    public void loadTexture(final int resourceDrawableID)
    {
        usingCameraImage = false;
        image = BitmapFactory.decodeResource(getResources(), resourceDrawableID);
        sprite = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight());
    }

    //////////////////////////////////////////////////
    //                  Set Texture                 //
    //==============================================//
    //  This will set the current resource file     //
    //  that we have passed in, as a texture for    //
    //  the sprite.                                 //
    //////////////////////////////////////////////////
    public void setTexture(Vector2 textureCoords, Vector2 textureDimen)
    {
        float imageUConversion = image.getWidth();
        float imageVConversion = image.getHeight();

        // Where the image will start from on the spritesheet.
        textureCoordinates = new Vector2((textureCoords.getX() * imageUConversion), (textureCoords.getY() * imageVConversion));

        // Setting the width and height of each sprite.
        // This should be the same for the whole sprite sheet really.
        // Unless there are exceptionally big sprites/characters/things.
        textureDimensions = new Vector2((textureDimen.getX() * imageUConversion), (textureDimen.getY() * imageVConversion));
    }

    //////////////////////////////////////////////////
    //                  Remove Texture              //
    //==============================================//
    //  This will "remove" the current texture of   //
    //  the sprite.                                 //
    //////////////////////////////////////////////////
    public void removeTexture()
    {
        // Setting the image as a transparent texture for level clearing.
        image = BitmapFactory.decodeResource(getResources(), R.drawable.transparent_sprite);
        sprite = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight());
        postInvalidate();
    }

    //////////////////////////////////////////////////
    //                  Change Texture              //
    //==============================================//
    //  This will change the current texture        //
    //  coordinates of the sprite.                  //
    //  This is used for animation.                 //
    //////////////////////////////////////////////////
    public void changeTexture(Vector2 textureCoords)
    {
        float imageUConversion = image.getWidth();
        float imageVConversion = image.getHeight();

        textureCoordinates.set(textureCoords.getX() * imageUConversion, textureCoords.getY() * imageVConversion);
        postInvalidate();
    }

    //////////////////////////////////////////////////
    //                  On Draw                     //
    //==============================================//
    //  This will draw the sprite onto the screen   //
    //  for us.                                     //
    //////////////////////////////////////////////////
    @Override
    protected void onDraw(final Canvas gameCanvas)
    {
        super.onDraw(gameCanvas);

        // If there is not a sprite image.
        if(sprite == null)
        {
            // Set a standard rectangle up.
            test.set((int)getSpriteLeft(), (int)getSpriteTop(), (int)getSpriteRight(), (int)getSpriteBottom());

            // Draw the rectangle with a colour.
            gameCanvas.drawRect(test, colour);
        }
        // Otherwise, there is a sprite image.
        else
        {
            // Set up the initial sprite of the image.
            // This is the initial source of the sprite, use uv coordinates for dimensions.
            test.set((int)textureCoordinates.getX(), (int)textureCoordinates.getY(), (int)textureCoordinates.getX() + (int)textureDimensions.getX(), (int)textureCoordinates.getY() + (int)textureDimensions.getY());

            // Use destRect for animations.
            destRect.set((int)getSpriteLeft(), (int)getSpriteTop(), (int)getSpriteRight(), (int)getSpriteBottom());
            gameCanvas.rotate(angle, getSpriteCenter().getX(), getSpriteCenter().getY());
            gameCanvas.drawBitmap(sprite, test, destRect, null);
        }
    }

    // Getters.
    // This will return the current sprite x coordinate.
    public float getSpriteLeft()            { return position.getX(); }

    // This will return the current sprite y coordinate.
    public float getSpriteTop()             { return position.getY(); }

    // This will return the current right coordinate of the sprite.
    public float getSpriteRight()           { return (position.getX() + dimension.getX()); }

    // This will return the current bottom coordinate of the sprite.
    public float getSpriteBottom()          { return (position.getY() + dimension.getY()); }

    // This will return the center of the sprite.
    public Vector2 getSpriteCenter()        { return (new Vector2(position.getX() + (dimension.getX() * 0.5f), position.getY() + (dimension.getY() * 0.5f))); }

    // This will return the current width of the sprite.
    public float getSpriteWidth()           { return dimension.getX(); }

    // This will return the current height of the sprite.
    public float getSpriteHeight()          { return dimension.getY(); }

    // This will return whether or not we are using the camera with this sprite.
    public boolean isUsingCameraImage()     { return usingCameraImage; }

    // This will return our current image for the sprite.
    public Bitmap getImage()                { return image; }

    // This will return our current colour for the sprite.
    public Paint getColour()                { return colour; }
}

