package com.draga.spaceTravels3.component.physicsComponent;

import com.badlogic.gdx.utils.Pools;
import com.draga.PooledVector2;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.gameEntity.GameEntityGroup;

import java.io.Serializable;

public class PhysicsComponent implements Serializable
{
    private final boolean affectedByGravity;

    private final PhysicsComponentType physicsComponentType;

    private final PooledVector2 position;
    private final PooledVector2 velocity;

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
        boolean affectedByGravity,
        PhysicsComponentType physicsComponentType)
    {
        this.ownerClass = ownerClass;
        this.physicsComponentType = physicsComponentType;
        this.position = PooledVector2.newVector2(x, y);
        this.velocity = PooledVector2.newVector2(0f, 0f);
        this.mass = mass;
        this.boundsCircle = new Circle(boundsRadius);
        this.collidesWith = collidesWith;
        this.affectedByGravity = affectedByGravity;
    }

    public PhysicsComponent(PhysicsComponent originalPhysicsComponent)
    {
        this.ownerClass = originalPhysicsComponent.ownerClass;
        this.position = originalPhysicsComponent.position.cpy();
        this.velocity = originalPhysicsComponent.velocity.cpy();
        this.mass = originalPhysicsComponent.mass;
        this.boundsCircle = new Circle(originalPhysicsComponent.getBoundsCircle().radius);
        this.collidesWith = new GameEntityGroup(originalPhysicsComponent.collidesWith);
        this.affectedByGravity = originalPhysicsComponent.affectedByGravity;
        this.physicsComponentType = originalPhysicsComponent.physicsComponentType;
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

    public PooledVector2 getPosition()
    {
        return position;
    }

    public PooledVector2 getVelocity()
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
        this.position.close();
        this.velocity.close();
    }

    public float getMass()
    {
        return mass;
    }

    public GameEntityGroup getCollidesWith()
    {
        return collidesWith;
    }

    public PhysicsComponentType getPhysicsComponentType()
    {
        return physicsComponentType;
    }
}
