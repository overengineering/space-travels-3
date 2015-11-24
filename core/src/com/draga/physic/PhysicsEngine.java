package com.draga.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.entity.GameEntity;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.entity.Star;
import com.draga.entity.shape.Circle;
import com.draga.event.ShipPlanetCollisionEvent;
import com.draga.event.StarCollectedEvent;
import com.draga.manager.GameEntityManager;
import com.google.common.base.Stopwatch;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class PhysicsEngine
{
    private static final String LOGGING_TAG =
        PhysicsEngine.class.getSimpleName();

    private static final int       MIN_STEPS     = 1;
    private static final int       MAX_STEPS     = 10;
    private static final int       FPS_GOAL      = 60;
    private static final Stopwatch UPDATE_TIMER  = Stopwatch.createUnstarted();
    private static       int       CURRENT_STEPS = MAX_STEPS;
    private static float updateTime;

    public static float getUpdateTime()
    {
        return updateTime;
    }

    public static int getCurrentSteps()
    {
        return CURRENT_STEPS;
    }

    public static void update(float elapsed)
    {
        UPDATE_TIMER.start();


        if (Gdx.graphics.getFramesPerSecond() >= FPS_GOAL)
        {
            if (CURRENT_STEPS < MAX_STEPS)
            {
                CURRENT_STEPS++;
            }
        }
        else
        {
            if (CURRENT_STEPS > MIN_STEPS)
            {
                CURRENT_STEPS--;
            }
        }

        for (float step = 0; step < CURRENT_STEPS; step++)
        {
            GameEntityManager.update();
            step(elapsed / CURRENT_STEPS);
        }

        updateTime = UPDATE_TIMER.elapsed(TimeUnit.NANOSECONDS) * Constants.NANO;
        UPDATE_TIMER.reset();
    }

    /**
     * Performs a physic step.
     *
     * @param elapsed
     */
    private static void step(float elapsed)
    {
        // Updates all position according to the game entity velocity.
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.physicsComponent.getPosition()
                .add(gameEntity.physicsComponent.getVelocity().cpy().scl(elapsed));
        }

        /** Check for collisions following this pattern to avoid duplicates:
         *
         *    \ X0 \ X1 \ X2
         * Y0 \    \    \
         * Y1 \ X  \    \
         * Y2 \ X  \ X  \
         */
        for (int x = 1; x < GameEntityManager.getGameEntities().size(); x++)
        {
            GameEntity gameEntityA = GameEntityManager.getGameEntities().get(x);
            for (int y = 0; y < x; y++)
            {
                GameEntity gameEntityB = GameEntityManager.getGameEntities().get(y);
                if (areColliding(gameEntityA, gameEntityB))
                {
                    resolveCollision(gameEntityA, gameEntityB);
                }
            }
        }
    }

    private static boolean areColliding(
        GameEntity gameEntityA,
        GameEntity gameEntityB)
    {
        if (!(gameEntityA.physicsComponent.getShape() instanceof Circle)
            || !(gameEntityB.physicsComponent.getShape() instanceof Circle))
        {
            throw new NotImplementedException();
        }

        Circle circleA = (Circle) gameEntityA.physicsComponent.getShape();
        Circle circleB = (Circle) gameEntityB.physicsComponent.getShape();

        double maxDistance = (circleA.radius) + (circleB.radius);

        if (gameEntityA.physicsComponent.getPosition()
            .dst(gameEntityB.physicsComponent.getPosition()) < maxDistance)
        {
            return true;
        }

        return false;
    }

    private static void resolveCollision(
        GameEntity gameEntityA,
        GameEntity gameEntityB)
    {
        boolean solved = twoWayInstanceSolver(
            gameEntityA,
            gameEntityB,
            Ship.class,
            Planet.class,
            "resolveShipPlanetCollision");
        if (!solved)
        {
            twoWayInstanceSolver(
                gameEntityA,
                gameEntityB,
                Star.class,
                "resolveShipStarCollision");
        }
        //        if (gameEntityA instanceof Ship
        //            && gameEntityB instanceof Planet)
        //        {
        //            resolveShipPlanetCollision((Ship) gameEntityA, (Planet) gameEntityB);
        //        }
        //        else if (gameEntityA instanceof Planet
        //            && gameEntityB instanceof Ship)
        //        {
        //            resolveShipPlanetCollision((Ship) gameEntityB, (Planet) gameEntityA);
        //        }
        // else
        //        if (gameEntityA instanceof Star
        //            && gameEntityB instanceof Ship)
        //        {
        //            resolveShipStarCollision((Star) gameEntityA);
        //        }
        //        else if (gameEntityA instanceof Ship
        //            && gameEntityB instanceof Star)
        //        {
        //            resolveShipStarCollision((Star) gameEntityB);
        //        }
    }

    private static boolean twoWayInstanceSolver(
        GameEntity gameEntityA,
        GameEntity gameEntityB,
        Class<? extends GameEntity> classA,
        Class<? extends GameEntity> classB,
        String methodName)
    {
        if (classA.isInstance(gameEntityA)
            && classB.isInstance(gameEntityB))
        {
            try
            {
                Method method =
                    PhysicsEngine.class.getDeclaredMethod(methodName, classA, classB);
                try
                {
                    method.invoke(null, gameEntityA, gameEntityB);
                    return true;
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                } catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        else if (classA.isInstance(gameEntityB)
            && classB.isInstance(gameEntityA))
        {
            try
            {
                Method method = PhysicsEngine.class.getDeclaredMethod(methodName, classA, classB);
                try
                {
                    method.invoke(null, gameEntityB, gameEntityA);
                    return true;
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                } catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean twoWayInstanceSolver(
        GameEntity gameEntityA,
        GameEntity gameEntityB,
        Class<? extends GameEntity> classA,
        String methodName)
    {
        if (classA.isInstance(gameEntityA))
        {
            try
            {
                Method method =
                    PhysicsEngine.class.getDeclaredMethod(methodName, classA);
                try
                {
                    method.invoke(null, gameEntityA);
                    return true;
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                } catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        else if (classA.isInstance(gameEntityB))
        {
            try
            {
                Method method = PhysicsEngine.class.getDeclaredMethod(methodName, classA);
                try
                {
                    method.invoke(null, gameEntityB);
                    return true;
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                } catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void resolveShipPlanetCollision(Ship ship, Planet planet)
    {
        ShipPlanetCollisionEvent shipPlanetCollisionEvent =
            Pools.obtain(ShipPlanetCollisionEvent.class);
        shipPlanetCollisionEvent.ship = ship;
        shipPlanetCollisionEvent.planet = planet;

        Constants.EVENT_BUS.post(shipPlanetCollisionEvent);
        Pools.free(shipPlanetCollisionEvent);
    }

    private static void resolveShipStarCollision(Star star)
    {
        StarCollectedEvent starCollectedEvent = Pools.obtain(StarCollectedEvent.class);
        starCollectedEvent.set(star);
        Constants.EVENT_BUS.post(starCollectedEvent);
        Pools.free(starCollectedEvent);
    }
    
    public static Vector2 getForceActingOn(GameEntity gameEntity)
    {
        Vector2 totalForce = new Vector2();
        
        for (GameEntity otherGameEntity : GameEntityManager.getGameEntities())
        {
            if (!gameEntity.equals(otherGameEntity))
            {
                Vector2 force = getGravityForce(gameEntity, otherGameEntity);
                totalForce.add(force);
            }
        }
        
        return totalForce;
    }
    
    private static Vector2 getGravityForce(
        GameEntity gameEntityA,
        GameEntity gameEntityB)
    {
        Vector2 distance =
            gameEntityB.physicsComponent.getPosition()
                .cpy()
                .sub(gameEntityA.physicsComponent.getPosition());
        float distanceLen2 = distance.len2();
        distance = distance.nor();
        float forceMagnitude =
            gameEntityA.physicsComponent.getMass() * gameEntityB.physicsComponent.getMass()
                / distanceLen2;
        distance = distance.scl(forceMagnitude);
        return distance;
    }
}
