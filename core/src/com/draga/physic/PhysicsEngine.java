package com.draga.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;
import com.draga.gameEntity.GameEntity;
import com.draga.manager.GameEntityManager;
import com.draga.manager.SettingsManager;
import com.draga.physic.shape.Circle;
import com.google.common.base.Stopwatch;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhysicsEngine
{
    private static final String LOGGING_TAG =
        PhysicsEngine.class.getSimpleName();

    private static final int       MIN_STEPS     = 1;
    private static final int       MAX_STEPS     = 10;
    private static final int       FPS_GOAL      = 60;
    private static       Stopwatch updateTimer   = Stopwatch.createUnstarted();
    private static       int       CURRENT_STEPS = MAX_STEPS;
    private static float                        updateTime;
    private static HashMap<GameEntity, Vector2> calculatedGravityForces;

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
        updateTimer.start();


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

        updateTime = updateTimer.elapsed(TimeUnit.NANOSECONDS) * Constants.NANO;
        updateTimer.reset();
    }

    /**
     * Performs a physic step.
     *
     * @param deltaTime
     */
    private static void step(float deltaTime)
    {
        calculatedGravityForces.clear();

        if (!SettingsManager.getDebugSettings().noGravity)
        {
            for (GameEntity gameEntity : GameEntityManager.getGameEntities())
            {
                if (gameEntity.physicsComponent.affectedByGravity)
                {
                    stepGravity(gameEntity, deltaTime);
                }
            }
        }

        // Updates all position according to the game entity velocity.
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.physicsComponent.getPosition()
                .add(gameEntity.physicsComponent.getVelocity().cpy().scl(deltaTime));
            gameEntity.physicsComponent.setAngle(gameEntity.physicsComponent.getAngle()
                + gameEntity.physicsComponent.getAngularVelocity() * deltaTime);
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
                if (gameEntityA.physicsComponent.getCollisionGroup().contains(gameEntityB)
                    && gameEntityB.physicsComponent.getCollisionGroup().contains(gameEntityA)
                    && areColliding(gameEntityA, gameEntityB))
                {
                    Gdx.app.debug(
                        LOGGING_TAG,
                        "Collision between "
                            + gameEntityA.getClass().getSimpleName()
                            + " and "
                            + gameEntityB.getClass().getSimpleName());
                    CollisionResolver.resolve(gameEntityA, gameEntityB);
                }
            }
        }
    }

    private static void stepGravity(GameEntity gameEntity, float deltaTime)
    {
        Vector2 gravityForce = getGravityForceActingOn(gameEntity);

        gameEntity.physicsComponent.getVelocity().add(gravityForce.scl(deltaTime));
    }

    private static boolean areColliding(
        GameEntity gameEntityA,
        GameEntity gameEntityB)
    {
        if (!(gameEntityA.physicsComponent.getShape() instanceof Circle)
            || !(gameEntityB.physicsComponent.getShape() instanceof Circle))
        {
            throw new UnsupportedOperationException();
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

    private static Vector2 calculateGravityForceActingOn(GameEntity gameEntity)
    {
        Vector2 totalForce = new Vector2();

        for (GameEntity otherGameEntity : GameEntityManager.getGameEntities())
        {
            if (!gameEntity.equals(otherGameEntity))
            {
                Vector2 force = calculateGravityForce(gameEntity, otherGameEntity);
                totalForce.add(force);
            }
        }

        return totalForce;
    }

    private static Vector2 calculateGravityForce(
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

    /**
     * Gets the gravity on a game entity. Calculates it if it has not been already, otherwise returns
     * the last one.
     * @param gameEntity
     * @return
     */
    public static Vector2 getGravityForceActingOn(GameEntity gameEntity)
    {
        if (!calculatedGravityForces.containsKey(gameEntity))
        {
            Vector2 force = calculateGravityForceActingOn(gameEntity);
            calculatedGravityForces.put(gameEntity, force);
        }

        return calculatedGravityForces.get(gameEntity).cpy();
    }

    public static void create()
    {
        calculatedGravityForces = new HashMap<>();
        updateTimer = Stopwatch.createUnstarted();
    }
    
    public static void dispose()
    {

    }
}
