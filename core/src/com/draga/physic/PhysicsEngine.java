package com.draga.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.draga.Constants;
import com.draga.Timer;
import com.draga.entity.GameEntity;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.entity.Star;
import com.draga.entity.shape.Circle;
import com.draga.event.ShipPlanetCollisionEvent;
import com.draga.event.StarCollectedEvent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class PhysicsEngine
{
    private static final String LOGGING_TAG =
        PhysicsEngine.class.getSimpleName();

    private static final ArrayList<GameEntity> GAME_ENTITIES           = new ArrayList<>();
    private static final ArrayList<GameEntity> GAME_ENTITIES_TO_REMOVE = new ArrayList<>();
    private static final ArrayList<GameEntity> GAME_ENTITIES_TO_CREATE = new ArrayList<>();

    private static final int MIN_STEPS     = 1;
    private static final int MAX_STEPS     = 10;
    private static final int FPS_GOAL      = 60;
    private static final Timer TIMER = new Timer();
    private static       int CURRENT_STEPS = MIN_STEPS;
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
        TIMER.start();

        // Remove all game entities marked for removal.
        GAME_ENTITIES.removeAll(GAME_ENTITIES_TO_REMOVE);
        for (GameEntity gameEntity : GAME_ENTITIES_TO_REMOVE)
        {
            gameEntity.dispose();
        }
        GAME_ENTITIES_TO_REMOVE.clear();

        // Add game entities.
        GAME_ENTITIES.addAll(GAME_ENTITIES_TO_CREATE);
        GAME_ENTITIES_TO_CREATE.clear();

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
            step(elapsed / CURRENT_STEPS);
        }

        updateTime = TIMER.elapsed();
    }

    private static void step(float elapsed)
    {
        // Updates all position according to the game entity velocity.
        for (GameEntity gameEntity : GAME_ENTITIES)
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
        for (int x = 1; x < GAME_ENTITIES.size(); x++)
        {
            GameEntity gameEntityA = GAME_ENTITIES.get(x);
            for (int y = 0; y < x; y++)
            {
                GameEntity gameEntityB = GAME_ENTITIES.get(y);
                checkCollision(gameEntityA, gameEntityB);
            }
        }
    }

    private static void checkCollision(
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
            resolveCollision(gameEntityA, gameEntityB);
        }
    }

    private static void resolveCollision(
        GameEntity gameEntityA,
        GameEntity gameEntityB)
    {
        if (gameEntityA instanceof Ship
            && gameEntityB instanceof Planet)
        {
            resolveShipPlanetCollision((Ship) gameEntityA, (Planet) gameEntityB);
        }
        else if (gameEntityA instanceof Planet
            && gameEntityB instanceof Ship)
        {
            resolveShipPlanetCollision((Ship) gameEntityB, (Planet) gameEntityA);
        }
        else if (gameEntityA instanceof Star
            && gameEntityB instanceof Ship)
        {
            resolveShipStarCollision((Star) gameEntityA);
        }
        else if (gameEntityA instanceof Ship
            && gameEntityB instanceof Star)
        {
            resolveShipStarCollision((Star) gameEntityB);
        }
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
        
        for (GameEntity otherGameEntity : GAME_ENTITIES)
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

    public static ArrayList<GameEntity> getGameEntities()
    {
        return GAME_ENTITIES;
    }

    public static void addGameEntity(GameEntity gameEntity)
    {
        GAME_ENTITIES_TO_CREATE.add(gameEntity);
    }

    public static void removeGameEntity(GameEntity gameEntity)
    {
        GAME_ENTITIES_TO_REMOVE.add(gameEntity);
    }

    public static void dispose()
    {

        for (GameEntity gameEntity : GAME_ENTITIES)
        {
            gameEntity.dispose();
        }

        GAME_ENTITIES.clear();
        GAME_ENTITIES_TO_REMOVE.clear();
        GAME_ENTITIES_TO_CREATE.clear();
    }
}
