// The package location for this class.
package com.example.app.jason.ragerelease.app.Framework.Physics;

// All of the extra includes here.
import com.example.app.jason.ragerelease.app.Framework.AnimatedSprite;
import com.example.app.jason.ragerelease.app.Framework.Maths.Vector2;
import com.example.app.jason.ragerelease.app.Framework.Resources;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * Created by Jason Mottershead on 06/07/2015.
 */

// Dynamic Body IS AN Animated Sprite, therefore inherits from it.
public class DynamicBody extends AnimatedSprite
{
    // Methods.
    //////////////////////////////////////////////////
    //                  Constructor                 //
    //==============================================//
    //  This will initialise the animated sprite    //
    //  object.                                     //
    //////////////////////////////////////////////////
    public DynamicBody(final Resources gameResources, int objectID)
    {
        super(gameResources, objectID);
    }

    //////////////////////////////////////////////////
    //                  Body Init                   //
    //==============================================//
    //  This will initialise the Box2D dynamic      //
    //  body.                                       //
    //////////////////////////////////////////////////
    public void bodyInit(Vector2 positions, Vector2 dimensions, float angle)
    {
        // Initialising the sprite.
        init(positions, dimensions, angle);
        spawnLocation = new Vector2(positions.getX(), positions.getY());

        // Setting up the body definition.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(new Vec2(getBox2DXPosition(getSpriteLeft()), getBox2DYPosition(getSpriteTop())));
        bodyDef.angle = (angle * ((float) Math.PI / 180.0f));
        body = new Body(bodyDef, resources.getWorld());
        body = resources.getWorld().createBody(bodyDef);
        body.setFixedRotation(true);
        body.setTransform(new Vec2(getBox2DXPosition(getSpriteLeft()), getBox2DYPosition(getSpriteTop())), bodyDef.angle);

        PolygonShape box = new PolygonShape();
        box.setAsBox(getBox2DSize(dimensions.getX()) * box2DDynamicBodyXOffset, getBox2DSize(dimensions.getY()) * box2DDynamicBodyYOffset);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.2f;
        body.createFixture(fixtureDef);

        // Setting the connection between sprites and the body.
        body.setUserData(this);
    }

    //////////////////////////////////////////////////
    //                  Update Body                 //
    //==============================================//
    //  This will update the position of the body   //
    //  and sprite based on gravity and any forces  //
    //  applied to it.                              //
    //////////////////////////////////////////////////
    public void updateBody()
    {
        float newX = body.getPosition().x;
        float newY = body.getPosition().y;

        setPosition(getFrameworkXPosition(newX), getFrameworkYPosition(newY));
    }
}
