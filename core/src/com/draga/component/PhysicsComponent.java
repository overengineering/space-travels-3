package com.draga.component;

import com.badlogic.gdx.math.Vector2;
import com.draga.gameEntity.GameEntityGroup;
import com.draga.physic.shape.Shape;

public class PhysicsComponent
{
    public final boolean affectedByGravity;

    private final float           mass;
    private final Vector2         position;
    private final Vector2         velocity;
    private final Shape           shape;
    private final GameEntityGroup collisionGroup;
    private       float           angle;
    private       float           angularVelocity;

    public PhysicsComponent(
        float x,
        float y,
        float mass,
        Shape shape,
        GameEntityGroup collisionGroup,
        boolean affectedByGravity)
    {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.mass = mass;
        this.shape = shape;
        this.collisionGroup = collisionGroup;
        this.affectedByGravity = affectedByGravity;
    }

    public float getAngularVelocity()
    {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity)
    {
        this.angularVelocity = angularVelocity;
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

    public void dispose()
    {
    }

    public float getMass()
    {
        return mass;
    }

    public GameEntityGroup getCollisionGroup()
    {
        return collisionGroup;
    }
}
