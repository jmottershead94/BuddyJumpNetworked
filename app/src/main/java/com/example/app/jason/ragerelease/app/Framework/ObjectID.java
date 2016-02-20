// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework;

/**
 * Created by Jason Mottershead on 24/07/2015.
 */

// This is a base ID class.
// This will only be used to differentiate between objects, and help filter out collisions.
public class ObjectID
{
    // Attributes.
    // Public.
    public static final int CHARACTERONE = 0;                   // The ID number for the first character.
    public static final int CHARACTERTWO = CHARACTERONE + 1;    // The ID number for the second character.
    public static final int ANIMATEDGROUND = CHARACTERTWO + 1;  // The ID number for the animated ground.
    public static final int GROUND = ANIMATEDGROUND + 1;        // The ID number for the static ground.
    public static final int WALL = GROUND + 1;                  // The ID number for the walls.
    public static final int ENEMY = WALL + 1;                   // The ID number for the enemy.
    public static final int SPRITE = ENEMY + 1;                 // The ID number for a standard sprite.
    public static final int ANIMATEDSPRITE = SPRITE + 1;        // The ID number for an animated sprite.
    public static final int OBSTACLE = ANIMATEDSPRITE + 1;      // The ID number for the obstacles.
}
