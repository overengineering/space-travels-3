package com.draga.entity;

import com.badlogic.gdx.math.Vector2;
import com.draga.entity.shape.Shape;

public class PhysicsComponent
{
    private final float mass;
    private final Vector2 position;
    private final Vector2 velocity;
    private float angle;
    private final Shape shape;

    public PhysicsComponent(float x, float y, float mass, Shape shape)
    {
        this.position = new Vector2(x, y);
        this.shape = shape;
        this.mass = mass;
        this.velocity = new Vector2();

        PhysicsEngine.register(this);
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public float getAngle()
    {
        return angle;
    }

    public void setAngle(float angle)
    {
        this.angle = angle;
    }

    public Shape getShape()
    {
        return shape;
    }

    public boolean collides(PhysicsComponent otherPhysicsComponent)
    {
        return CollisionResolver.resolve(this, otherPhysicsComponent);
    }

    public void dispose()
    {
        PhysicsEngine.unregister(this);
    }

    public float getMass()
    {
        return mass;
    }
}
