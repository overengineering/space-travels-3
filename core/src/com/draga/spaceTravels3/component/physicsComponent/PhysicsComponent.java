package com.draga.spaceTravels3.component.physicsComponent;

import com.badlogic.gdx.math.Vector2;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.gameEntity.GameEntityGroup;

import java.io.Serializable;

public class PhysicsComponent implements Serializable
{
    private final boolean affectedByGravity;

    private final PhysicsComponentType physicsComponentType;

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
        boolean affectedByGravity,
        PhysicsComponentType physicsComponentType)
    {
        this.ownerClass = ownerClass;
        this.physicsComponentType = physicsComponentType;
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0f, 0f);
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

    public Circle getBoundsCircle()
    {
        return this.boundsCircle;
    }

    public static String s(String s)
    {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++)
        {
            char c = chars[i];
            if (c >= 'a' && c <= 'm')
            {
                c += 13;
            }
            else if (c >= 'A' && c <= 'M')
            {
                c += 13;
            }
            else if (c >= 'n' && c <= 'z')
            {
                c -= 13;
            }
            else if (c >= 'N' && c <= 'Z')
            {
                c -= 13;
            }
            chars[i] = c;
        }

        return new String(chars);
    }

    public Class<? extends GameEntity> getOwnerClass()
    {
        return this.ownerClass;
    }

    public boolean isAffectedByGravity()
    {
        return this.affectedByGravity;
    }

    public float getAngularVelocity()
    {
        return this.angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity)
    {
        this.angularVelocity = angularVelocity;
    }

    public Vector2 getPosition()
    {
        return this.position;
    }

    public Vector2 getVelocity()
    {
        return this.velocity;
    }

    public float getAngle()
    {
        return this.angle;
    }

    public void setAngle(float angle)
    {
        this.angle = angle;
    }

    public void dispose()
    {
    }

    public float getMass()
    {
        return this.mass;
    }

    public GameEntityGroup getCollidesWith()
    {
        return this.collidesWith;
    }

    public PhysicsComponentType getPhysicsComponentType()
    {
        return this.physicsComponentType;
    }
}
