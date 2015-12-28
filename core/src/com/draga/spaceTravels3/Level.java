package com.draga.spaceTravels3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pools;
import com.draga.spaceTravels3.component.PhysicsComponent;
import com.draga.spaceTravels3.event.*;
import com.draga.spaceTravels3.gameEntity.*;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.SettingsManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.Projection;
import com.draga.spaceTravels3.physic.ProjectionPoint;
import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Level
{
    private static final String LOGGING_TAG = Level.class.getSimpleName();

    private final float trajectorySeconds;
    private final float maxLandingSpeed;

    private final ArrayList<Pickup> pickups;
    private final Ship              ship;
    private final Thruster          thruster;
    private final Planet            destinationPlanet;

    private final Rectangle bounds;

    private int pickupsCollected;
    private GameState gameState;

    private Stopwatch elapsedPlayTime;

    private Sound pickupCollectedSound;

    private String id;
    private String name;

    public Level(
        String id,
        String name,
        Ship ship,
        Thruster thruster,
        ArrayList<Planet> planets,
        ArrayList<Pickup> pickups,
        Planet destinationPlanet,
        float trajectorySeconds,
        float maxLandingSpeed)
    {
        this.ship = ship;
        this.thruster = thruster;
        this.destinationPlanet = destinationPlanet;
        this.pickups = pickups;
        this.id = id;
        this.name = name;
        this.trajectorySeconds = trajectorySeconds;
        this.maxLandingSpeed = maxLandingSpeed;

        this.gameState = GameState.COUNTDOWN;

        pickupCollectedSound = AssMan.getAssMan().get(AssMan.getAssList().pickupCollectSound);

        GameEntityManager.addGameEntity(thruster);
        GameEntityManager.addGameEntity(ship);
        for (Planet planet : planets)
        {
            GameEntityManager.addGameEntity(planet);
        }
        for (Pickup pickup : pickups)
        {
            GameEntityManager.addGameEntity(pickup);
        }
        GameEntityManager.update();

        this.bounds = getBounds(
            this.ship.physicsComponent.getPosition().x,
            this.ship.physicsComponent.getPosition().y);

        Constants.General.EVENT_BUS.register(this);

        elapsedPlayTime = Stopwatch.createUnstarted();
    }

    /**
     * Return a rectangle that includes all the physic components and a little buffer.
     * @param x
     * @param y
     */
    private Rectangle getBounds(float x, float y)
    {
        Rectangle bounds = new Rectangle(
            x,
            y,
            0,
            0);

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            PhysicsComponent physicsComponent = gameEntity.physicsComponent;
            Rectangle gameEntityBounds = new Rectangle(
                physicsComponent.getPosition().x - physicsComponent.getBoundsCircle().radius,
                physicsComponent.getPosition().y - physicsComponent.getBoundsCircle().radius,
                physicsComponent.getBoundsCircle().radius * 2f,
                physicsComponent.getBoundsCircle().radius * 2f);
            bounds.merge(gameEntityBounds);
        }
        bounds.width += Constants.Game.LEVEL_BOUNDS_BUFFER;
        bounds.height += Constants.Game.LEVEL_BOUNDS_BUFFER;
        bounds.x -= Constants.Game.LEVEL_BOUNDS_BUFFER;
        bounds.y -= Constants.Game.LEVEL_BOUNDS_BUFFER;

        return bounds;
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        pickupsCollected++;
        pickupCollectedSound.play(SettingsManager.getSettings().volume);
        GameEntityManager.removeGameEntity(pickupCollectedEvent.pickup);
    }

    @Subscribe
    public void shipPlanetCollision(ShipPlanetCollisionEvent shipPlanetCollisionEvent)
    {
        if (Constants.General.IS_DEBUGGING)
        {
            Gdx.app.debug(
                LOGGING_TAG,
                "Linear velocity on collision: " + ship.physicsComponent.getVelocity().len());
        }

        GameEntityManager.removeGameEntity(ship);
        GameEntityManager.removeGameEntity(thruster);

        if (ship.physicsComponent.getVelocity().len()
            > this.getMaxLandingSpeed()
            || !shipPlanetCollisionEvent.planet.equals(destinationPlanet))
        {
            gameState = GameState.LOSE;
            GameEntity explosion = new Explosion(
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().x,
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().y,
                shipPlanetCollisionEvent.ship.graphicComponent.getWidth(),
                shipPlanetCollisionEvent.ship.graphicComponent.getHeight()
            );
            GameEntityManager.addGameEntity(explosion);

            LoseEvent loseEvent = Pools.obtain(LoseEvent.class);
            Constants.General.EVENT_BUS.post(loseEvent);
            Pools.free(loseEvent);
        }
        else
        {
            GameEntityManager.removeGameEntity(ship);
            GameEntityManager.removeGameEntity(thruster);

            gameState = GameState.WIN;

            WinEvent winEvent = Pools.obtain(WinEvent.class);
            Constants.General.EVENT_BUS.post(winEvent);
            Pools.free(winEvent);
        }
    }

    public int getScore()
    {
        float pickupPoints = pickupsCollected * Constants.Game.PICKUP_POINTS;
        float timePoints = elapsedPlayTime.elapsed(TimeUnit.NANOSECONDS)
            * Constants.General.NANO
            * Constants.Game.TIME_POINTS;
        float fuelPoints = ship.isInfiniteFuel()
            ? 0
            : ship.getCurrentFuel() / ship.getMaxFuel() * Constants.Game.FUEL_POINTS;

        float score = pickupPoints;
        score -= timePoints;
        score += fuelPoints;
        return Math.round(score);
    }

    public String getId()
    {
        return id;
    }

    @Subscribe
    public void countdownFinished(CountdownFinishedEvent countdownFinishedEvent)
    {
        if (gameState == GameState.COUNTDOWN)
        {
            this.gameState = GameState.PLAY;
            elapsedPlayTime.start();
        }
    }

    public void dispose()
    {
        Constants.General.EVENT_BUS.unregister(this);

        pickupCollectedSound.stop();
        pickupCollectedSound.dispose();
    }

    public Ship getShip()
    {
        return ship;
    }

    public float getWidth()
    {
        return bounds.getWidth();
    }

    public float getHeight()
    {
        return bounds.getHeight();
    }

    public ArrayList<Pickup> getPickups()
    {
        return pickups;
    }

    public GameState getGameState()
    {
        return gameState;
    }

    public void pause()
    {
        if (gameState == GameState.PLAY
            || gameState == GameState.COUNTDOWN)
        {
            gameState = GameState.PAUSE;
            if (elapsedPlayTime.isRunning())
            {
                elapsedPlayTime.stop();
            }
        }
    }

    public void resume()
    {
        if (gameState == GameState.PAUSE)
        {
            gameState = GameState.COUNTDOWN;
        }
    }

    public Projection processProjection(ArrayList<ProjectionPoint> projectionPoints)
    {
        ArrayList<Vertex> vertices = new ArrayList<>(projectionPoints.size());

        int lastCollisionIndex = 0;
        // Keep a list of PhysicsComponent that we already collided with to exclude them later on.
        ArrayList<PhysicsComponent> alreadyCollidedPhysicsComponents = new ArrayList<>();
        for (int i = 0, projectionPointsSize = projectionPoints.size(); i
            < projectionPointsSize; i++)
        {
            ProjectionPoint projectionPoint = projectionPoints.get(i);

            // Get the PhysicsComponent we are colliding with at this point excluding the one
            // already checked for.
            ArrayList<PhysicsComponent> physicsComponentsToCheck =
                new ArrayList<>(projectionPoint.getCollidingPhysicsComponents());
            physicsComponentsToCheck.removeAll(alreadyCollidedPhysicsComponents);

            // If it collides with something or this is the last iteration.
            if (!physicsComponentsToCheck.isEmpty()
                || i == projectionPointsSize - 1)
            {
                Color currentColor = getColor(projectionPoint, physicsComponentsToCheck);
                alreadyCollidedPhysicsComponents.addAll(physicsComponentsToCheck);

                // Add all the vertices up to this collision with the correct color.
                for (int j = lastCollisionIndex; j < i; j++)
                {
                    vertices.add(
                        j,
                        new Vertex(currentColor, projectionPoints.get(j).getPosition()));
                }
                lastCollisionIndex = i;

                // If collided with a planet truncate here.
                for (PhysicsComponent physicsComponent : physicsComponentsToCheck)
                {
                    if (physicsComponent.getOwnerClass().equals(Planet.class))
                    {
                        return new Projection(vertices);
                    }
                }
            }
        }

        return new Projection(vertices);
    }

    private Color getColor(
        ProjectionPoint projectionPoint,
        ArrayList<PhysicsComponent> nextCollidingPhysicsComponents)
    {
        for (PhysicsComponent nextCollidingPhysicsComponent : nextCollidingPhysicsComponents)
        {
            if (nextCollidingPhysicsComponent.getOwnerClass().equals(Planet.class))
            {
                // If colliding with destination planet lerps from
                // the color for winning (zero velocity),
                // to white (max approach speed)
                // to the color for losing (twice the maximum approach velocity)
                if (destinationPlanet.physicsComponent.equals(nextCollidingPhysicsComponent))
                {
                    return Constants.Visual.HUD.TrajectoryLine.COLOR_PLANET_DESTINATION;
                }
                else
                {
                    return Constants.Visual.HUD.TrajectoryLine.COLOR_PLANET_LOSE;
                }
            }
        }

        for (PhysicsComponent nextCollidingPhysicsComponent : nextCollidingPhysicsComponents)
        {
            if (nextCollidingPhysicsComponent.getOwnerClass().equals(Pickup.class))
            {
                return Constants.Visual.HUD.TrajectoryLine.COLOR_PICKUP;
            }
        }

        return Constants.Visual.HUD.TrajectoryLine.COLOR_NEUTRAL;
    }

    public Planet getDestinationPlanet()
    {
        return destinationPlanet;
    }

    public float getTrajectorySeconds()
    {
        return trajectorySeconds;
    }

    public float getMaxLandingSpeed()
    {
        return maxLandingSpeed;
    }
}
