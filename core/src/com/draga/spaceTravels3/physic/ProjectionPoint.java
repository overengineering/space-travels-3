package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.utils.Pool;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

import java.util.ArrayList;

public class ProjectionPoint implements Pool.Poolable
{
    private PooledVector2               position;
    private ArrayList<PhysicsComponent> collidingPhysicsComponents;

    public void set(
        PooledVector2 position,
        ArrayList<PhysicsComponent> collidingPhysicsComponents)
    {
        this.position = position;
        this.collidingPhysicsComponents = collidingPhysicsComponents;
    }

    public ArrayList<PhysicsComponent> getCollidingPhysicsComponents()
    {
        return this.collidingPhysicsComponents;
    }

    public PooledVector2 getPosition()
    {
        return this.position;
    }

    @Override
    public void reset()
    {
        this.position.close();
        this.position = null;
        this.collidingPhysicsComponents = null;
    }
}
