package com.draga.entity;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class PhysicsEngine
{
    static final private ArrayList<PhysicsComponent> physicsComponents = new ArrayList<>();
    
    public static void register(PhysicsComponent physicsComponent)
    {
        physicsComponents.add(physicsComponent);
    }
    
    public static void update(float elapsed)
    {
        for (PhysicsComponent physicsComponent : physicsComponents)
        {
            physicsComponent.getPosition().add(physicsComponent.getVelocity().cpy().scl(elapsed));
        }
    }
    
    public static void unregister(PhysicsComponent physicsComponent)
    {
        physicsComponents.remove(physicsComponent);
    }
    
    public static Vector2 getForceActingOn(PhysicsComponent physicsComponent)
    {
        Vector2 totalForce = new Vector2();
        
        for (PhysicsComponent otherPhysicsComponent : physicsComponents)
        {
            if (physicsComponent != otherPhysicsComponent)
            {
                Vector2 force = getGravityForce(physicsComponent, otherPhysicsComponent);
                totalForce.add(force);
            }
        }
        
        return totalForce;
    }
    
    private static Vector2 getGravityForce(
        PhysicsComponent physicsComponentA,
        PhysicsComponent physicsComponentB)
    {
        Vector2 distance =
            physicsComponentB.getPosition().cpy().sub(physicsComponentA.getPosition());
        float distanceLen2 = distance.len2();
        distance = distance.nor();
        float forceMagnitude =
            physicsComponentA.getMass() * physicsComponentB.getMass() / distanceLen2;
        distance = distance.scl(forceMagnitude);
        return distance;
    }
}
