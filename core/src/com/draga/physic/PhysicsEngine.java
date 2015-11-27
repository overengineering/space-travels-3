package com.draga.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.draga.Constants;
import com.draga.gameEntity.GameEntity;
import com.draga.physic.shape.Circle;
import com.draga.manager.GameEntityManager;
import com.google.common.base.Stopwatch;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
            gameEntity.physicsComponent.setAngle(gameEntity.physicsComponent.getAngle()
                + gameEntity.physicsComponent.getAngularVelocity() * elapsed);
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
