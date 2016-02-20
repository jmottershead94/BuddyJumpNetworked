// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Maths;

/**
 * Created by Jason Mottershead on 14/06/2015.
 */

// This is a vector2 class for handling any of our local maths calculations.
// This is the same vector2 class adapted from the OpenGL coursework from 2nd year of study.
public class Vector2
{
    // Attributes.
    // Private.
    private float[] elements = new float[2];

    // Constructor.
    public Vector2(float x, float y)
    {
        this.elements[0] = x;
        this.elements[1] = y;
    }

    // Returns the length of the 2D vector.
    public float length()
    {
        return (float) Math.sqrt(this.lengthSquared());
    }

    // Returns the length squared of the 2D vector.
    public float lengthSquared()
    {
        return (this.elements[0]*this.elements[0] +
                this.elements[1]*this.elements[1]);
    }

    public Vector2 normalize()
    {
        float mag = this.length();

        if(mag != 0.0f)
        {
            float multiplier = 1.0f / mag;
            this.elements[0] *= multiplier;
            this.elements[1] *= multiplier;
        }

        return this;
    }

    public Vector2 cross(final Vector2 vec2)
    {
        Vector2 cross = new Vector2(this.elements[0] * vec2.elements[1] - this.elements[1] * vec2.elements[0], 0.0f);

        return cross;
    }

    public Vector2 subtract(final Vector2 vec2, float scale)
    {
        Vector2 sub = new Vector2(this.elements[0] - (vec2.elements[0] * scale),
                                  this.elements[1] - (vec2.elements[1] * scale));

        return sub;
    }

    public Vector2 set(float x, float y)
    {
        this.elements[0] = x;
        this.elements[1] = y;

        return this;
    }

    public Vector2 setX(float x)
    {
        this.elements[0] = x;
        return this;
    }

    public Vector2 setY(float y)
    {
        this.elements[1] = y;
        return this;
    }

    public float getX()
    {
        return this.elements[0];
    }

    public float getY()
    {
        return this.elements[1];
    }

    public float dotProduct(final Vector2 vec2)
    {
        return (this.elements[0] * vec2.elements[0] +
                this.elements[1] * vec2.elements[1]);
    }

    public Vector2 scale(float scale)
    {
        Vector2 scaled = new Vector2(this.elements[0] * scale,
                                     this.elements[1] * scale);

        return scaled;
    }

    public Vector2 add(final Vector2 vec2, float scale)
    {
        Vector2 sum = new Vector2(this.elements[0] + (vec2.elements[0] * scale),
                                  this.elements[1] + (vec2.elements[1] * scale));

        return sum;
    }

    public Vector2 addEquals(final Vector2 vec2)
    {
        this.elements[0] += vec2.elements[0];
        this.elements[1] += vec2.elements[1];

        return this;
    }

    public Vector2 subtractEquals(final Vector2 vec2)
    {
        this.elements[0] -= vec2.elements[0];
        this.elements[1] -= vec2.elements[1];

        return this;
    }

    public Vector2 multiplyEquals(final Vector2 vec2)
    {
        this.elements[0] *= vec2.elements[0];
        this.elements[1] *= vec2.elements[1];

        return this;
    }
}
