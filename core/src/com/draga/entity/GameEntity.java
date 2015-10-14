package com.draga.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class GameEntity
{
    public    Box2dCollisionResolutionComponent collisionResolutionComponent;
    protected BodyDef                           bodyDef;
    protected Body                              body;

    public abstract void update(float deltaTime);

    public abstract void draw(SpriteBatch spriteBatch);

    public abstract void dispose();

    public abstract void createBody(World world);

    public float getX()
    {
        return body.getPosition().x;
    }

    public float getY()
    {
        return body.getPosition().y;
    }

    public Body getBody()
    {
        return body;
    }
}
