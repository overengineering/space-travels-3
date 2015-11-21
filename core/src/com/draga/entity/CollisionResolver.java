package com.draga.entity;

import com.draga.entity.shape.Circle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CollisionResolver
{
    public static boolean resolve(PhysicsComponent physicalComponentA, PhysicsComponent physicalComponentB)
    {
        if (!(physicalComponentA.getShape() instanceof Circle)
            || !(physicalComponentB.getShape() instanceof Circle))
        {
            throw new NotImplementedException();
        }

        Circle circleA = (Circle) physicalComponentA.getShape();
        Circle circleB = (Circle) physicalComponentB.getShape();

        double maxDistance = (circleA.radius) + (circleB.radius);

        return physicalComponentA.getPosition().dst(physicalComponentB.getPosition()) < maxDistance;
    }
}
