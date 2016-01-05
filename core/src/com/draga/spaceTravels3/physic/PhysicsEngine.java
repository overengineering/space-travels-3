package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.draga.PooledVector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhysicsEngine
{
    private static final String             LOGGING_TAG                            =
        PhysicsEngine.class.getSimpleName();
    private static final PerformanceCounter GRAVITY_PROJECTION_PERFORMANCE_COUNTER =
        new PerformanceCounter("gravityProjection", 60);
    private static final PerformanceCounter STEP_PERFORMANCE_COUNTER               =
        new PerformanceCounter("step", 60);
    private static HashMap<PhysicsComponent, CollisionCache>
                       physicsComponentCollisionCache;
    private static GravityCache gravityCache;

    public static PerformanceCounter getStepPerformanceCounter()
    {
        return STEP_PERFORMANCE_COUNTER;
    }

    public static void update(float elapsed)
    {
        STEP_PERFORMANCE_COUNTER.start();
        
        for (int step = 0; step < Constants.Game.PHYSICS_STEPS; step++)
        {
            GameEntityManager.update();
            step(elapsed / Constants.Game.PHYSICS_STEPS);
        }

        STEP_PERFORMANCE_COUNTER.stop();
        STEP_PERFORMANCE_COUNTER.tick();
    }
    
    /**
     * Performs a physic step. Applies gravity, velocity and angular velocity, then checks for
     * collisions.
     */
    private static void step(float deltaTime)
    {
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            if (gameEntity.physicsComponent.getPhysicsComponentType()
                == PhysicsComponentType.DYNAMIC)
            {
                stepPhysicsComponent(gameEntity.physicsComponent, deltaTime);
            }
        }
        
        checkCollisions();
    }
    
    /**
     * Steps the gravity (if enables and is affected by), velocity and angular velocity for a
     * {@link PhysicsComponent}
     */
    private static void stepPhysicsComponent(PhysicsComponent physicsComponent, float deltaTime)
    {
        if (!SettingsManager.getDebugSettings().noGravity
            && physicsComponent.isAffectedByGravity())
        {
            try(PooledVector2 gravity = gravityCache.getCachedGravity(physicsComponent))
            {
                physicsComponent.getVelocity().add(gravity.scl(deltaTime));
            }
        }
        
        applyVelocity(physicsComponent, deltaTime);
        applyAngularVelocity(physicsComponent, deltaTime);
        
    }
    
    /**
     * Check for collisions following this pattern to avoid duplicates:
     * ___|_X0_|_X1_|_X2_
     * Y0 |
     * Y1 | X
     * Y2 | X    X
     * Collision checks are skipped between static objects.
     */
    private static void checkCollisions()
    {
        for (int x = 1; x < GameEntityManager.getGameEntities().size(); x++)
        {
            GameEntity gameEntityA = GameEntityManager.getGameEntities().get(x);

            if (physicsComponentCollisionCache.containsKey(gameEntityA.physicsComponent))
            {
                CollisionCache collisionCache =
                    physicsComponentCollisionCache.get(gameEntityA.physicsComponent);
                ArrayList<PhysicsComponent> possibleCollidingStaticPhysicsComponents =
                    collisionCache.getPossibleCollidingPhysicsComponents(
                        gameEntityA.physicsComponent.getPosition().x,
                        gameEntityA.physicsComponent.getPosition().y);
                for (int y = 0; y < x; y++)
                {
                    GameEntity gameEntityB = GameEntityManager.getGameEntities().get(y);
                    if (gameEntityB.physicsComponent.getPhysicsComponentType()
                        == PhysicsComponentType.DYNAMIC
                        || possibleCollidingStaticPhysicsComponents.contains(gameEntityB.physicsComponent))
                    {
                        if (areColliding(
                            gameEntityA.physicsComponent,
                            gameEntityB.physicsComponent))
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
            else
            {
                for (int y = 0; y < x; y++)
                {
                    GameEntity gameEntityB = GameEntityManager.getGameEntities().get(y);
                    if (gameEntityA.physicsComponent.getPhysicsComponentType()
                        == PhysicsComponentType.DYNAMIC
                        || gameEntityB.physicsComponent.getPhysicsComponentType()
                        == PhysicsComponentType.DYNAMIC)
                    {
                        if (areColliding(
                            gameEntityA.physicsComponent,
                            gameEntityB.physicsComponent))
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
        }
    }
    
    protected static ArrayList<PhysicsComponent> getAllPhysicsComponentsExcept(PhysicsComponent excludePhysicsComponent)
    {
        ArrayList<PhysicsComponent> physicsComponents = getAllPhysicsComponents();
        physicsComponents.remove(excludePhysicsComponent);
        
        return physicsComponents;
    }
    
    /**
     * Accelerate the {@link PhysicsComponent} according to the gravity acting on it.
     */
    private static void stepGravity(
        PhysicsComponent physicsComponent,
        ArrayList<PhysicsComponent> otherPhysicsComponents, float deltaTime)
    {
        try (PooledVector2 gravityForce = calculateGravityForce(
            physicsComponent,
            otherPhysicsComponents))
        {
            physicsComponent.getVelocity().add(gravityForce.scl(deltaTime));
        }
    }
    
    /**
     * Applies the {@link PhysicsComponent}'s velocity to its position.
     */
    private static void applyVelocity(PhysicsComponent physicsComponent, float deltaTime)
    {
        try (PooledVector2 velocity = physicsComponent.getVelocity().cpy())
        {
            physicsComponent.getPosition()
                .add(velocity.scl(deltaTime));
        }
    }
    
    /**
     * Applies the {@link PhysicsComponent}'s angular velocity to its rotation.
     */
    private static void applyAngularVelocity(PhysicsComponent physicsComponent, float deltaTime)
    {
        physicsComponent.setAngle(physicsComponent.getAngle()
            + physicsComponent.getAngularVelocity() * deltaTime);
    }
    
    /**
     * Check if both {@link PhysicsComponent} can collide with each other and if the two are
     * overlapping.
     */
    protected static boolean areColliding(
        PhysicsComponent physicsComponentA,
        PhysicsComponent physicsComponentB)
    {
        return physicsComponentA.getCollidesWith().contains(physicsComponentB.getOwnerClass())
            && physicsComponentB.getCollidesWith().contains(physicsComponentA.getOwnerClass())
            && areOverlapping(physicsComponentA, physicsComponentB);
    }
    
    public static ArrayList<PhysicsComponent> getAllPhysicsComponents()
    {
        ArrayList<PhysicsComponent> physicsComponents = new ArrayList<>();
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            physicsComponents.add(gameEntity.physicsComponent);
        }
        
        return physicsComponents;
    }
    
    public static PooledVector2 calculateGravityForce(
        PhysicsComponent physicsComponent,
        ArrayList<PhysicsComponent> otherPhysicComponents)
    {
        PooledVector2 force = PooledVector2.newVector2(0f, 0f);

        for (PhysicsComponent otherPhysicsComponent : otherPhysicComponents)
        {
            addGravityForce(physicsComponent, otherPhysicsComponent, force);
        }
        
        return force;
    }

    private static boolean areOverlapping(
        PhysicsComponent physicsComponentA,
        PhysicsComponent physicsComponentB)
    {
        float overlappingDistance =
            physicsComponentA.getBoundsCircle().radius + physicsComponentB.getBoundsCircle().radius;
        float distance2 = physicsComponentA.getPosition().dst2(physicsComponentB.getPosition());

        boolean areOverlapping = distance2 <= overlappingDistance * overlappingDistance;

        return areOverlapping;
    }
    
    private static void addGravityForce(
        PhysicsComponent physicsComponentA,
        PhysicsComponent physicsComponentB,
        PooledVector2 force)
    {
        float x = physicsComponentB.getPosition().x - physicsComponentA.getPosition().x;
        float y = physicsComponentB.getPosition().y - physicsComponentA.getPosition().y;

        float len2 = x * x + y * y;
        double len = Math.sqrt(len2);

        float scale = physicsComponentA.getMass()
            * physicsComponentB.getMass()
            / len2;

        x *= scale / len;
        y *= scale / len;

        force.add(x, y);
    }
    
    public static void create()
    {
        physicsComponentCollisionCache = new HashMap<>();
    }
    
    public static void dispose()
    {
        physicsComponentCollisionCache = null;
    }
    
    public static PerformanceCounter getGravityProjectionPerformanceCounter()
    {
        return GRAVITY_PROJECTION_PERFORMANCE_COUNTER;
    }

    public static ArrayList<ProjectionPoint> gravityProjection(
        PhysicsComponent originalPhysicsComponent,
        float projectionSeconds,
        float pointTime)
    {
        GRAVITY_PROJECTION_PERFORMANCE_COUNTER.start();

        int points = Math.round(projectionSeconds / pointTime);

        // Create a copy of the PhysicsComponent so that it won't be changed.
        PhysicsComponent physicsComponent = new PhysicsComponent(originalPhysicsComponent);

        ArrayList<PhysicsComponent> otherPhysicsComponents =
            getAllPhysicsComponentsExcept(originalPhysicsComponent);

        ArrayList<ProjectionPoint> projectionPoints = new ArrayList<>(points);

        for (int i = 0; i < points; i++)
        {
            // Sub points to make the physics calculations more accurate without adding visual points.
            float stepTime = Constants.Visual.HUD.TrajectoryLine.STEP_TIME;

            ArrayList<PhysicsComponent> collidingPhysicsComponents = new ArrayList<>();
            for (float remainingStepTime = pointTime; remainingStepTime > 0;
                 remainingStepTime -= stepTime)
            {
                if (remainingStepTime < stepTime)
                {
                    stepTime = remainingStepTime;
                }

                try(PooledVector2 gravity = gravityCache.getCachedGravity(physicsComponent))
                {
                    physicsComponent.getVelocity().add(gravity.scl(stepTime));
                }

                applyVelocity(physicsComponent, stepTime);

                // If we have collision with static physComp cached..
                if (physicsComponentCollisionCache.containsKey(originalPhysicsComponent))
                {
                    CollisionCache collisionCache =
                        physicsComponentCollisionCache.get(originalPhysicsComponent);
                    ArrayList<PhysicsComponent> possibleCollidingPhysicsComponents =
                        collisionCache.getPossibleCollidingPhysicsComponents(
                            physicsComponent.getPosition().x,
                            physicsComponent.getPosition().y);
                    for (PhysicsComponent otherPhysicsComponent : otherPhysicsComponents)
                    {
                        if (!collidingPhysicsComponents.contains(otherPhysicsComponent))
                        {
                            if (otherPhysicsComponent.getPhysicsComponentType()
                                == PhysicsComponentType.DYNAMIC
                                && areColliding(physicsComponent, otherPhysicsComponent))
                            {
                                collidingPhysicsComponents.add(otherPhysicsComponent);
                            }
                            else if (possibleCollidingPhysicsComponents.contains(
                                otherPhysicsComponent)
                                && areColliding(physicsComponent, otherPhysicsComponent))
                            {
                                collidingPhysicsComponents.add(otherPhysicsComponent);
                            }
                        }

                    }
                }
                // If we DON'T have collision with static physComp cached..
                else
                {
                    for (PhysicsComponent otherPhysicsComponent : otherPhysicsComponents)
                    {
                        if (!collidingPhysicsComponents.contains(otherPhysicsComponent)
                            && areColliding(physicsComponent, otherPhysicsComponent))
                        {
                            collidingPhysicsComponents.add(otherPhysicsComponent);
                        }
                    }
                }
            }

            ProjectionPoint projectionPoint = new ProjectionPoint(
                physicsComponent.getPosition().cpy(),
                collidingPhysicsComponents);
            projectionPoints.add(projectionPoint);
        }

        GRAVITY_PROJECTION_PERFORMANCE_COUNTER.stop();
        GRAVITY_PROJECTION_PERFORMANCE_COUNTER.tick();

        return projectionPoints;
    }
    
    public static PooledVector2 calculateGravityForce(PhysicsComponent physicsComponent)
    {
        ArrayList<PhysicsComponent> otherPhysicsComponents =
            getAllPhysicsComponentsExcept(physicsComponent);
        PooledVector2 gravityForce =
            calculateGravityForce(physicsComponent, otherPhysicsComponents);

        return gravityForce;
    }

    public static void cachePhysicsComponentCollisions(PhysicsComponent physicsComponent)
    {
        CollisionCache collisionCache = new CollisionCache(physicsComponent);
        physicsComponentCollisionCache.put(physicsComponent, collisionCache);
    }

    public static void cacheGravity()
    {
        gravityCache = new GravityCache();
    }
}
