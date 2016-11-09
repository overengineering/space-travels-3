package com.draga.spaceTravels3.physic;

import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.event.PickupCollectedEvent;
import com.draga.spaceTravels3.event.ShipPlanetCollisionEvent;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.gameEntity.Pickup;
import com.draga.spaceTravels3.gameEntity.Planet;
import com.draga.spaceTravels3.gameEntity.Ship;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class CollisionResolver
{
    private static final String LOGGING_TAG =
        CollisionResolver.class.getSimpleName();

    /**
     * A list of the methods to resolve collisions.
     */
    private static final List<Method> collisionResolverDeclaredMethods =
        Arrays.asList(CollisionResolvers.class.getDeclaredMethods());

    /**
     * Resolve a collision between two game entities;
     */
    public static void resolve(GameEntity gameEntityA, GameEntity gameEntityB)
    {
        try
        {
            for (Method declaredMethod : collisionResolverDeclaredMethods)
            {
                if (declaredMethod.getParameterTypes()[0].isInstance(gameEntityA)
                    && declaredMethod.getParameterTypes()[1].isInstance(gameEntityB))
                {
                    declaredMethod.invoke(null, gameEntityA, gameEntityB);
                    break;
                }
                if (declaredMethod.getParameterTypes()[0].isInstance(gameEntityB)
                    && declaredMethod.getParameterTypes()[1].isInstance(gameEntityA))
                {
                    declaredMethod.invoke(null, gameEntityB, gameEntityA);
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            ErrorHandlerProvider.handle(LOGGING_TAG, "", e);
        }
    }

    /**
     * A collection of methods to resolve collisions, methods must have only two parameters
     * extending game entity.
     */
    private abstract static class CollisionResolvers
    {
        public static void resolveShipPlanetCollision(Ship ship, Planet planet)
        {
            Constants.General.EVENT_BUS.post(new ShipPlanetCollisionEvent(ship, planet));
        }

        public static void resolveShipPickupCollision(Ship ship, Pickup pickup)
        {
            Constants.General.EVENT_BUS.post(new PickupCollectedEvent(pickup));
        }
    }
}
