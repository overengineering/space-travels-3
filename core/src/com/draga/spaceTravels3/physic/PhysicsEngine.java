package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.draga.JavaUtils;
import com.draga.shape.Circle;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.PhysicsComponent;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.google.common.base.Stopwatch;
import com.sun.org.apache.bcel.internal.generic.GOTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhysicsEngine
{
    private static final String LOGGING_TAG =
        PhysicsEngine.class.getSimpleName();

    private static Stopwatch updateTimer = Stopwatch.createUnstarted();
    private static float                        updateTime;
    private static HashMap<GameEntity, Vector2> calculatedGravityForces;

    public static float getUpdateTime()
    {
        return updateTime;
    }

    public static void update(float elapsed)
    {
        updateTimer.start();

        for (int step = 0; step < Constants.Game.PHYSICS_STEPS; step++)
        {
            GameEntityManager.update();
            step(elapsed / Constants.Game.PHYSICS_STEPS);
        }

        updateTime = updateTimer.elapsed(TimeUnit.NANOSECONDS) * Constants.General.NANO;
        updateTimer.reset();
    }

    /**
     * Performs a physic step.
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

        // Updates all position and rotation according to the game entity velocity and
        // angular velocity.
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            gameEntity.physicsComponent.getPosition()
                .add(gameEntity.physicsComponent.getVelocity().cpy().scl(deltaTime));
            gameEntity.physicsComponent.setAngle(gameEntity.physicsComponent.getAngle()
                + gameEntity.physicsComponent.getAngularVelocity() * deltaTime);
        }

        checkCollisions();
    }

    private static void stepGravity(GameEntity gameEntity, float deltaTime)
    {
        Vector2 gravityForce = getGravityForceActingOn(gameEntity);

        gameEntity.physicsComponent.getVelocity().add(gravityForce.scl(deltaTime));
    }

    /**
     * Check for collisions following this pattern to avoid duplicates:
     * \ X0 \ X1 \ X2
     * Y0 \    \    \
     * Y1 \ X  \    \
     * Y2 \ X  \ X  \
     */
    private static void checkCollisions()
    {
        for (int x = 1; x < GameEntityManager.getGameEntities().size(); x++)
        {
            GameEntity gameEntityA = GameEntityManager.getGameEntities().get(x);
            for (int y = 0; y < x; y++)
            {
                GameEntity gameEntityB = GameEntityManager.getGameEntities().get(y);
                if (areColliding(gameEntityA, gameEntityB))
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

    /**
     * Gets the gravity on a game entity. Calculates it if it has not been already, otherwise returns
     * the last one.
     */
    public static Vector2 getGravityForceActingOn(GameEntity gameEntity)
    {
        if (!calculatedGravityForces.containsKey(gameEntity))
        {
            Vector2 force = calculateGravityForceActingOn(gameEntity.physicsComponent);
            calculatedGravityForces.put(gameEntity, force);
        }

        return calculatedGravityForces.get(gameEntity).cpy();
    }

    private static boolean areColliding(GameEntity gameEntityA, GameEntity gameEntityB)
    {
        return gameEntityA.physicsComponent.getCollisionGroup().contains(gameEntityB)
            && gameEntityB.physicsComponent.getCollisionGroup().contains(gameEntityA)
            && areOverlapping(gameEntityA.physicsComponent, gameEntityB.physicsComponent);
    }

    private static Vector2 calculateGravityForceActingOn(PhysicsComponent physicsComponent)
    {
        Vector2 totalForce = new Vector2();

        for (GameEntity otherGameEntity : GameEntityManager.getGameEntities())
        {
            PhysicsComponent otherPhysicsComponent = otherGameEntity.physicsComponent;

            if (!physicsComponent.equals(otherPhysicsComponent))
            {
                Vector2 force = calculateGravityForce(physicsComponent, otherPhysicsComponent);
                totalForce.add(force);
            }
        }

        return totalForce;
    }

    /**
     * @param physicsComponentA
     * @param physicsComponentB
     * @throws UnsupportedOperationException If physics component's Shape is not a Circle.
     */
    private static boolean areOverlapping(
        PhysicsComponent physicsComponentA,
        PhysicsComponent physicsComponentB)
    {
        // Rewrite the whole shebang when other Shapes are needed. Maybe in a way similar to
        // the collisionResolver.

        if (!(physicsComponentA.getShape() instanceof Circle)
            || !(physicsComponentB.getShape() instanceof Circle))
        {
            throw new UnsupportedOperationException();
        }

        Circle circleA = (Circle) physicsComponentA.getShape();
        Circle circleB = (Circle) physicsComponentB.getShape();

        float collisionDistance = circleA.radius + circleB.radius;

        boolean isColliding = physicsComponentA.getPosition()
            .dst(physicsComponentB.getPosition()) < collisionDistance;

        return isColliding;
    }

    private static Vector2 calculateGravityForce(
        PhysicsComponent physicsComponentA,
        PhysicsComponent physicsComponentB)
    {
        Vector2 distance = physicsComponentB.getPosition()
            .cpy()
            .sub(physicsComponentA.getPosition());
        float distanceLen2 = distance.len2();

        Vector2 direction = distance.nor();

        float force = physicsComponentA.getMass()
            * physicsComponentB.getMass()
            / distanceLen2;

        Vector2 gravityForce = direction.scl(force);

        return gravityForce;
    }

    public static void create()
    {
        calculatedGravityForces = new HashMap<>();
        updateTimer = Stopwatch.createUnstarted();
    }
    
    public static void dispose()
    {

    }

    public static ArrayList<Vector2> projectGravity(
        GameEntity gameEntity,
        int steps,
        float stepTime)
    {
        // Swap out physics component so to revert after projection.
        PhysicsComponent originalComponent = gameEntity.physicsComponent;
        gameEntity.physicsComponent = (PhysicsComponent) JavaUtils.deepClone(originalComponent);

        ArrayList<Vector2> projections = new ArrayList<>();

        buildProjections:
        for (int i = 0; i < steps; i++)
        {
            Vector2 force = calculateGravityForceActingOn(gameEntity.physicsComponent);

            // Accelerate by gravity.
            gameEntity.physicsComponent.getVelocity().add(force.scl(stepTime));

            // Move.
            gameEntity.physicsComponent.getPosition()
                .add(gameEntity.physicsComponent.getVelocity().cpy().scl(stepTime));

            projections.add(gameEntity.physicsComponent.getPosition().cpy());

            // Stop on collision.
            for (GameEntity otherGameEntity : GameEntityManager.getGameEntities())
            {
                if (!gameEntity.equals(otherGameEntity)
                    && areColliding(gameEntity, otherGameEntity))
                {
                    break buildProjections;
                }
            }

        }

        gameEntity.physicsComponent = originalComponent;

        return projections;
    }
}
