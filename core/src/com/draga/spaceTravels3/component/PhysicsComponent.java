package com.draga.spaceTravels3.component;

import com.badlogic.gdx.math.Vector2;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.gameEntity.GameEntityGroup;

import java.io.Serializable;

public class PhysicsComponent implements Serializable
{
    private final boolean affectedByGravity;

    private final Vector2 position;
    private final Vector2 velocity;

    private final Circle boundsCircle;

    private final float mass;

    private final GameEntityGroup             collidesWith;
    private final Class<? extends GameEntity> ownerClass;

    private float angle;
    private float angularVelocity;

    public PhysicsComponent(
        float x,
        float y,
        float mass,
        float boundsRadius,
        GameEntityGroup collidesWith,
        Class<? extends GameEntity> ownerClass,
        boolean affectedByGravity)
    {
        this.ownerClass = ownerClass;
        this.position = new Vector2(x, y);
        this.velocity = new Vector2();
        this.mass = mass;
        this.boundsCircle = new Circle(boundsRadius);
        this.collidesWith = collidesWith;
        this.affectedByGravity = affectedByGravity;
    }

    public PhysicsComponent(PhysicsComponent originalPhysicsComponent)
    {
        this.ownerClass = originalPhysicsComponent.ownerClass;
        this.position = new Vector2(originalPhysicsComponent.position);
        this.velocity = new Vector2(originalPhysicsComponent.velocity);
        this.mass = originalPhysicsComponent.mass;
        this.boundsCircle = new Circle(originalPhysicsComponent.getBoundsCircle().radius);
        this.collidesWith = new GameEntityGroup(originalPhysicsComponent.collidesWith);
        this.affectedByGravity = originalPhysicsComponent.affectedByGravity;
    }

    public Class<? extends GameEntity> getOwnerClass()
    {
        return ownerClass;
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

    public Circle getBoundsCircle()
    {
        return boundsCircle;
    }

    public void dispose()
    {
    }

    public float getMass()
    {
        return mass;
    }

    public GameEntityGroup getCollidesWith()
    {
        return collidesWith;
    }
}
