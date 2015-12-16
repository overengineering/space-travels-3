package com.draga.spaceTravels3.component;

import com.badlogic.gdx.math.Vector2;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.gameEntity.GameEntityGroup;

import java.io.Serializable;

public class PhysicsComponent implements Serializable
{
    public final boolean affectedByGravity;

    private final Vector2 position;
    private final Vector2 velocity;

    private final Circle shape;

    private final float mass;

    private final GameEntityGroup             collideWith;
    private final Class<? extends GameEntity> is;

    private float angle;
    private float angularVelocity;

    public PhysicsComponent(
        float x,
        float y,
        float mass,
        float radius,
        GameEntityGroup collideWith,
        Class<? extends GameEntity> is,
        boolean affectedByGravity)
    {
        this.is = is;
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.mass = mass;
        this.shape = new Circle(radius);
        this.collideWith = collideWith;
        this.affectedByGravity = affectedByGravity;
    }

    public Class<? extends GameEntity> getIs()
    {
        return is;
    }

    public boolean isAffectedByGravity()
    {
        return affectedByGravity;
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

    public Circle getShape()
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

    public GameEntityGroup getCollideWith()
    {
        return collideWith;
    }
}
