package com.draga.entity;

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
}
