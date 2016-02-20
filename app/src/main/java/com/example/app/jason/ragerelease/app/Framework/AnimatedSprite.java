// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework;

// All of the extra includes here.
import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 * Created by Jason Mottershead on 30/06/2015.
 */

// Animated Sprite IS A Sprite, therefore inherits from it.
public class AnimatedSprite extends Sprite
{
    // Attributes.
    // Protected.
    // Box2D conversion.
    protected static float box2DFrameworkScale = 100.0f;
    protected static float box2DFrameworkOffsetX = 0.0f;
    protected static float box2DFrameworkOffsetY = 0.0f;
    protected static float box2DStaticXOffset = 0.5f;
    protected static float box2DStaticYOffset = 0.25f;
    protected static float box2DDynamicBodyXOffset = 0.25f;
    protected static float box2DDynamicBodyYOffset = 0.75f;

    // Animated sprite.
    protected Body body = null;
    protected int id = 0;
    protected boolean respawn = false;
    protected Vector2 spawnLocation = null;
    protected Resources resources = null;

    // Private.
    private float animationFrameDuration = 0.1f;
    private float animationTime = 0.0f;
    private int numberOfFrames = 0;

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will initialise the sprite object, and //
    //  set up access to common game properties.    //
    //////////////////////////////////////////////////
    public AnimatedSprite(final Resources gameResources, int objectID)
    {
        super(gameResources.getContext());
        resources = gameResources;
        id = objectID;
    }

    //////////////////////////////////////////////////
    //               Animate Sprite                 //
    //==============================================//
    //  This will animate the sprite with the       //
    //  desired speed.                              //
    //////////////////////////////////////////////////
    public void animateSprite(float ticks)
    {
        // Add the time passed since the last update to the animation timer.
        //animationTime += ticks;
        animationTime += ticks;

        // If the animation timer has passed the length of time for one frame (in seconds).
        // Move to the next frame.
        if(animationTime > animationFrameDuration)
        {
            // Reset the animation timer.
            animationTime -= animationFrameDuration;

            // Move the texture coordinates to the next sprite on the sprite sheet.
            textureCoordinates.setX(textureCoordinates.getX() + textureDimensions.getX());

            // If we have reached the end of the animation.
            if(textureCoordinates.getX() > (numberOfFrames - 1) * textureDimensions.getX())
            {
                textureCoordinates.setX(0.0f);
            }

            // Redraws the new sprite.
            postInvalidate();
        }
    }

    //////////////////////////////////////////////////
    //                Translate Box2D               //
    //==============================================//
    //  This will translate both the animated       //
    //  sprite and the box2D body to a position,    //
    //  using the passed in Box2D position(meters). //
    //////////////////////////////////////////////////
    protected void translateBox2D(Vector2 box2DPosition)
    {
        setPosition(getFrameworkXPosition(box2DPosition.getX()), getFrameworkYPosition(box2DPosition.getY()));
        body.setTransform(new Vec2(box2DPosition.getX(), box2DPosition.getY()), 0.0f);
    }

    //////////////////////////////////////////////////
    //               Translate Framework            //
    //==============================================//
    //  This will translate both the animated       //
    //  sprite and the box2D body to a position,    //
    //  using the passed in framework position      //
    //  (pixel).                                    //
    //////////////////////////////////////////////////
    protected void translateFramework(Vector2 frameworkPosition)
    {
        setPosition(frameworkPosition.getX(), frameworkPosition.getY());
        body.setTransform(new Vec2(getBox2DXPosition(frameworkPosition.getX()), getBox2DYPosition(frameworkPosition.getY())), 0.0f);
    }

    // Setters.
    // This will set the current number of animation frames for the animated sprite.
    public void setAnimationFrames(int animationFrames)
    {
        numberOfFrames = animationFrames;
    }

    // Getters.
    // Getting FRAMEWORK COORDINATES.
    // Convert Box2D coordinates into local framework coordinates.
    public float getFrameworkXPosition(float box2DX)   { return ((box2DX * box2DFrameworkScale) + box2DFrameworkOffsetX); }
    public float getFrameworkYPosition(float box2DY)   { return (resources.getScreenHeight() - (box2DY * box2DFrameworkScale + box2DFrameworkOffsetY)); }
    public float getFrameworkSize(float dimension)     { return (dimension * box2DFrameworkScale); }

    // Getting BOX2D COORDINATES.
    // Convert local framework coordinates into Box2D coordinates.
    public float getBox2DXPosition(float frameworkX)   { return ((frameworkX - box2DFrameworkOffsetX) / box2DFrameworkScale); }
    public float getBox2DYPosition(float frameworkY)   { return ((frameworkY - resources.getScreenHeight() + box2DFrameworkOffsetY) / -box2DFrameworkScale); }
    public float getBox2DSize(float dimension)         { return (dimension / box2DFrameworkScale); }

    // This will return the current ID number of the animated sprite.
    public int getID()                  { return id; }

    // This will return the current respawn location of the animated sprite.
    public Vector2 getSpawnLocation()   { return spawnLocation; }
}
