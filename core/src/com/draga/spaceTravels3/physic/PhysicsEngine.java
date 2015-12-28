package com.draga.spaceTravels3.physic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.draga.spaceTravels3.Constants;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.spaceTravels3.gameEntity.GameEntity;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PhysicsEngine
{
    private static final String LOGGING_TAG =
        PhysicsEngine.class.getSimpleName();
    
    private static Stopwatch updateTimer = Stopwatch.createUnstarted();
    private static float updateTime;

    private static HashMap<PhysicsComponent, CollisionCache>
        physicsComponentCollisionCache;
    
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
            ArrayList<PhysicsComponent> otherPhysicsComponents =
                getAllPhysicsComponentsExcept(physicsComponent);
            
            stepGravity(physicsComponent, otherPhysicsComponents, deltaTime);
        }
        
        applyVelocity(physicsComponent, deltaTime);
        applyAngularVelocity(physicsComponent, deltaTime);
        
    }
    
    /**
     * Check for collisions following this pattern to avoid duplicates:
     * \ X0 \ X1 \ X2
     * Y0 \    \    \
     * Y1 \ X  \    \
     * Y2 \ X  \ X  \
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
        Vector2 gravityForce = calculateGravityForce(physicsComponent, otherPhysicsComponents);
        
        physicsComponent.getVelocity().add(gravityForce.scl(deltaTime));
    }
    
    /**
     * Applies the {@link PhysicsComponent}'s velocity to its position.
     */
    private static void applyVelocity(PhysicsComponent physicsComponent, float deltaTime)
    {
        physicsComponent.getPosition()
            .add(physicsComponent.getVelocity().cpy().scl(deltaTime));
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
    
    private static ArrayList<PhysicsComponent> getAllPhysicsComponents()
    {
        ArrayList<PhysicsComponent> physicsComponents = new ArrayList<>();
        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            physicsComponents.add(gameEntity.physicsComponent);
        }
        
        return physicsComponents;
    }
    
    public static Vector2 calculateGravityForce(
        PhysicsComponent physicsComponent,
        ArrayList<PhysicsComponent> otherPhysicComponents)
    {
        Vector2 totalForce = new Vector2();
        for (PhysicsComponent otherPhysicsComponent : otherPhysicComponents)
        {
            Vector2 force =
                calculateGravityForce(physicsComponent, otherPhysicsComponent);
            totalForce.add(force);
        }
        
        return totalForce;
    }
    
    private static boolean areOverlapping(
        PhysicsComponent physicsComponentA,
        PhysicsComponent physicsComponentB)
    {
        float overlappingDistance =
            physicsComponentA.getBoundsCircle().radius + physicsComponentB.getBoundsCircle().radius;
        float distance = physicsComponentA.getPosition().dst(physicsComponentB.getPosition());
        
        boolean areOverlapping = distance <= overlappingDistance;
        
        return areOverlapping;
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
        updateTimer = Stopwatch.createUnstarted();
        physicsComponentCollisionCache = new HashMap<>();
    }
    
    public static void dispose()
    {
        updateTimer = null;
        physicsComponentCollisionCache = null;
    }
    
    public static ArrayList<ProjectionPoint> gravityProjection(
        PhysicsComponent originalPhysicsComponent,
        float projectionSeconds,
        float pointTime)
    {
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

                stepGravity(physicsComponent, otherPhysicsComponents, stepTime);
                applyVelocity(physicsComponent, stepTime);

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
                        if (otherPhysicsComponent.getPhysicsComponentType()
                            == PhysicsComponentType.DYNAMIC)
                        {
                            if (!collidingPhysicsComponents.contains(otherPhysicsComponent)
                                && areColliding(physicsComponent, otherPhysicsComponent))
                            {
                                collidingPhysicsComponents.add(otherPhysicsComponent);
                            }
                        }
                        else
                        {
                            if (!collidingPhysicsComponents.contains(otherPhysicsComponent)
                                && possibleCollidingPhysicsComponents.contains(otherPhysicsComponent)
                                && areColliding(physicsComponent, otherPhysicsComponent))
                            {
                                collidingPhysicsComponents.add(otherPhysicsComponent);
                            }

                        }
                    }

                }
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
                physicsComponent.getVelocity().cpy(),
                collidingPhysicsComponents);
            projectionPoints.add(projectionPoint);
        }

        return projectionPoints;
    }
    
    public static Vector2 calculateGravityForce(PhysicsComponent physicsComponent)
    {
        ArrayList<PhysicsComponent> otherPhysicsComponents =
            getAllPhysicsComponentsExcept(physicsComponent);
        Vector2 gravityForce = calculateGravityForce(physicsComponent, otherPhysicsComponents);
        
        return gravityForce;
    }

    public static void cachePhysicsComponentCollisions(PhysicsComponent physicsComponent)
    {
        CollisionCache collisionCache = new CollisionCache(physicsComponent);
        physicsComponentCollisionCache.put(physicsComponent, collisionCache);
    }
}
