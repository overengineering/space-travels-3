package com.draga.spaceTravels3.physic;

import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;

import java.util.ArrayList;

public class CollisionCache
{
    public static final float GRANULARITY = 1f;
    private final ArrayList<PhysicsComponent>[][] collisions;

    public CollisionCache(ArrayList<PhysicsComponent>[][] collisions)
    {
        this.collisions = collisions;
    }

    public static float getGRANULARITY()
    {
        return GRANULARITY;
    }

    public ArrayList<PhysicsComponent>[][] getCollisions()
    {
        return collisions;
    }
}
