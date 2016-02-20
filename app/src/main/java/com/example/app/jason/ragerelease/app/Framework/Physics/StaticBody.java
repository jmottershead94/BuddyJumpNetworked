// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Physics;

// All of the extra includes here.
import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.app.Framework.Graphics.AnimatedSprite;
import com.example.app.jason.ragerelease.app.Framework.Resources;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

/**
 * Created by Jason Mottershead on 06/07/2015.
 */

// Static Body IS AN Animated Sprite, therefore inherits from it.
public class StaticBody extends AnimatedSprite
{
    // Attributes.
    // Private.
    private Vector2 newPosition = new Vector2(0.0f, 0.0f);

    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will initialise the animated sprite    //
    //  object.                                     //
    //////////////////////////////////////////////////
    public StaticBody(final Resources gameResources, int objectID)
    {
        super(gameResources, objectID);
    }

    //////////////////////////////////////////////////
    //                  Body Init                   //
    //==============================================//
    //  This will initialise the Box2D static body. //
    //////////////////////////////////////////////////
    public void bodyInit(Vector2 positions, Vector2 dimensions, float angle)
    {
        // Initialising the sprite.
        init(positions, dimensions, angle);
        spawnLocation = new Vector2(positions.getX(), positions.getY());

        // Setting up the body definition.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(new Vec2(getBox2DXPosition(getSpriteLeft()), getBox2DYPosition(getSpriteTop())));
        bodyDef.angle = (-1.0f * angle * ((float) Math.PI / 180.0f));

        // Setting up the body (inherited from AnimatedSprite).
        body = new Body(bodyDef, resources.getWorld());
        body = resources.getWorld().createBody(bodyDef);
        body.setTransform(new Vec2(getBox2DXPosition(getSpriteLeft()), getBox2DYPosition(getSpriteTop())), bodyDef.angle);

        // Creates the bounding box for the body.
        PolygonShape box = new PolygonShape();
        box.setAsBox(getBox2DSize(getSpriteWidth()) * box2DStaticXOffset, getBox2DSize(getSpriteHeight()) * box2DStaticYOffset);
        body.createFixture(box, 0.0f);

        // Setting the connection between sprites and the body.
        body.setUserData(this);
    }

    //////////////////////////////////////////////////
    //                  Update Body                 //
    //==============================================//
    //  This will update the position of the static //
    //  body based on the speed we pass in.         //
    //  This is used for the moving obstacles.      //
    //////////////////////////////////////////////////
    public void updateBody(Vector2 speed)
    {
        newPosition.set((body.getPosition().x + speed.getX()), (body.getPosition().y + speed.getY()));
        translateBox2D(newPosition);
    }
}
