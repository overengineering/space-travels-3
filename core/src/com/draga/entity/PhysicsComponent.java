package com.draga.entity;

import com.badlogic.gdx.math.Vector2;

public class PhysicsComponent
{
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    private float angle;
    private Shape shape;

    public PhysicsComponent(Shape shape)
    {
        this.shape = shape;

        PhysicsEngine.register(this);
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
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

    public void setShape(Shape shape)
    {
        this.shape = shape;
    }

    public boolean collides(PhysicsComponent otherPhysicsComponent)
    {
        return CollisionResolver.resolve(this, otherPhysicsComponent);
    }

    ;
}
