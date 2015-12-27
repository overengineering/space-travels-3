package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

import java.util.ArrayList;

public class ProjectionPoint
{
    private Vector2 position;
    private Vector2 velocity;
    private ArrayList<PhysicsComponent> collidingPhysicsComponents;

    public ProjectionPoint(
                Vector2 position,
         Vector2 velocity,
         ArrayList<PhysicsComponent> collidingPhysicsComponents)
    {
        this.position = position;
        this.velocity = velocity;
        this.collidingPhysicsComponents = collidingPhysicsComponents;
    }

    public ArrayList<PhysicsComponent> getCollidingPhysicsComponents()
    {
        return collidingPhysicsComponents;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public Vector2 getPosition()
    {
        return position;
    }
}
