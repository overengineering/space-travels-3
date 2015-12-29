package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.draga.Vector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

import java.util.ArrayList;

public class ProjectionPoint implements Pool.Poolable
{
    private Vector2                     position;
    private ArrayList<PhysicsComponent> collidingPhysicsComponents;

    public ProjectionPoint(
        Vector2 position,
        ArrayList<PhysicsComponent> collidingPhysicsComponents)
    {
        this.position = position;
        this.collidingPhysicsComponents = collidingPhysicsComponents;
    }

    public ArrayList<PhysicsComponent> getCollidingPhysicsComponents()
    {
        return collidingPhysicsComponents;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    @Override
    public void reset()
    {
        Pools.free(this.position);
        this.position = null;
        this.collidingPhysicsComponents = null;
    }
}
