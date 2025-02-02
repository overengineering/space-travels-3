package com.draga.spaceTravels3.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.draga.spaceTravels3.*;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponent;
import com.draga.spaceTravels3.component.physicsComponent.PhysicsComponentType;
import com.draga.spaceTravels3.event.*;
import com.draga.spaceTravels3.gameEntity.*;
import com.draga.spaceTravels3.manager.*;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.physic.Projection;
import com.draga.spaceTravels3.physic.ProjectionPoint;
import com.draga.spaceTravels3.screen.CountdownScreen;
import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Level
{
    private static final String LOGGING_TAG = Level.class.getSimpleName();

    private final float trajectorySeconds;
    private final float maxLandingSpeed;

    private final String playCompletionAchievementID;
    private final String playLeaderboardID;

    private final ArrayList<Pickup> pickups;
    private final Ship              ship;
    private final Thruster          thruster;
    private final String            difficulty;
    private       Planet            destinationPlanet;
    private       Rectangle         bounds;
    private       int               pickupsCollected;

    private GameState gameState;
    private Stopwatch elapsedPlayTime;

    private Sound pickupCollectedSound;

    private String id;
    private String name;

    private boolean tutorial;

    public Level(
        String id,
        String name,
        String difficulty,
        Ship ship,
        Thruster thruster,
        ArrayList<Planet> planets,
        ArrayList<Pickup> pickups,
        Planet destinationPlanet,
        float trajectorySeconds,
        float maxLandingSpeed,
        boolean tutorial,
        String playCompletionAchievementID,
        String playLeaderboardID)
    {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;

        this.ship = ship;
        this.thruster = thruster;
        this.destinationPlanet = destinationPlanet;
        this.pickups = pickups;
        this.trajectorySeconds = trajectorySeconds;
        this.maxLandingSpeed = maxLandingSpeed;
        this.tutorial = tutorial;
        this.playCompletionAchievementID = playCompletionAchievementID;
        this.playLeaderboardID = playLeaderboardID;

        this.gameState = GameState.PAUSE;

        this.pickupCollectedSound =
            AssMan.getGameAssMan().get(AssMan.getAssList().pickupCollectSound);

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

        calculateBounds();

        Constants.General.EVENT_BUS.register(this);

        this.elapsedPlayTime = Stopwatch.createUnstarted();
    }

    /**
     * Return a rectangle that includes all the physic components and a little buffer.
     */
    public void calculateBounds()
    {
        this.bounds = null;

        for (GameEntity gameEntity : GameEntityManager.getGameEntities())
        {
            PhysicsComponent physicsComponent = gameEntity.physicsComponent;
            if (physicsComponent.getPhysicsComponentType() == PhysicsComponentType.STATIC)
            {
                Rectangle gameEntityBounds = new Rectangle(
                    physicsComponent.getPosition().x - physicsComponent.getBoundsCircle().radius,
                    physicsComponent.getPosition().y - physicsComponent.getBoundsCircle().radius,
                    physicsComponent.getBoundsCircle().radius * 2f,
                    physicsComponent.getBoundsCircle().radius * 2f);
                if (this.bounds == null)
                {
                    this.bounds = gameEntityBounds;
                }
                else
                {
                    this.bounds.merge(gameEntityBounds);
                }
            }
        }

        // If we have found no static phyComp avoid leaving bounds null.
        if (this.bounds == null)
        {
            PhysicsComponent physicsComponent = this.ship.physicsComponent;
            this.bounds = new Rectangle(
                physicsComponent.getPosition().x - physicsComponent.getBoundsCircle().radius,
                physicsComponent.getPosition().y - physicsComponent.getBoundsCircle().radius,
                physicsComponent.getBoundsCircle().radius * 2f,
                physicsComponent.getBoundsCircle().radius * 2f);
        }

        this.bounds.x -= Constants.Game.LEVEL_BOUNDS_BUFFER;
        this.bounds.y -= Constants.Game.LEVEL_BOUNDS_BUFFER;
        this.bounds.height += Constants.Game.LEVEL_BOUNDS_BUFFER * 2f;
        this.bounds.width += Constants.Game.LEVEL_BOUNDS_BUFFER * 2f;
    }

    public Rectangle getBounds()
    {
        return this.bounds;
    }

    @Subscribe
    public void pickupCollected(PickupCollectedEvent pickupCollectedEvent)
    {
        this.pickupsCollected++;
        this.pickupCollectedSound.play(SettingsManager.getSettings().volumeFX);
        GameEntityManager.removeGameEntity(pickupCollectedEvent.pickup);
    }

    @Subscribe
    public void shipPlanetCollision(ShipPlanetCollisionEvent shipPlanetCollisionEvent)
    {
        if (this.tutorial)
        {
            return;
        }

        float shipVelocity = this.ship.physicsComponent.getVelocity().len();
        if (Constants.General.IS_DEBUGGING)
        {
            Gdx.app.debug(
                LOGGING_TAG,
                "Linear velocity on collision: " + shipVelocity);
        }

        this.elapsedPlayTime.stop();

        GameEntityManager.removeGameEntity(this.ship);
        GameEntityManager.removeGameEntity(this.thruster);

        boolean shipIsTooFast = shipVelocity
            > this.getMaxLandingSpeed();
        boolean planetIsDestination =
            shipPlanetCollisionEvent.planet.equals(this.destinationPlanet);
        if (shipIsTooFast
            || !planetIsDestination)
        {
            this.gameState = GameState.LOSE;
            Gdx.input.vibrate(500);
            GameEntity explosion = new Explosion(
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().x,
                shipPlanetCollisionEvent.ship.physicsComponent.getPosition().y,
                shipPlanetCollisionEvent.ship.graphicComponent.getWidth(),
                shipPlanetCollisionEvent.ship.graphicComponent.getHeight(),
                AssMan.getGameAssMan()
                    .get(AssMan.getAssList().explosionTextureAtlas, TextureAtlas.class)
            );
            GameEntityManager.addGameEntity(explosion);

            LoseEvent loseEvent = new LoseEvent(planetIsDestination, shipIsTooFast);
            Constants.General.EVENT_BUS.post(loseEvent);
        }
        else
        {
            this.gameState = GameState.WIN;

            Score score = getScore();
            Integer previousBestScore = ScoreManager.getScore(this.getId(), this.getDifficulty());

            Constants.General.EVENT_BUS.post(new WinEvent(score, previousBestScore));

            if (this.pickupsCollected == this.pickups.size())
            {
                SpaceTravels3.services.googleUnlockAchievement(this.playCompletionAchievementID);
            }

            SpaceTravels3.services.googleUpdateLeaderboard(
                this.playLeaderboardID,
                score.getTotalScore());

            ScoreManager.saveHighScore(
                this.getId(),
                this.getDifficulty(),
                score.getTotalScore());
        }
    }

    public float getMaxLandingSpeed()
    {
        return this.maxLandingSpeed;
    }

    public Score getScore()
    {
        Score score = new Score(
            this.pickupsCollected,
            this.elapsedPlayTime.elapsed(TimeUnit.NANOSECONDS) * MathUtils.nanoToSec,
            this.ship.isInfiniteFuel()
                ? 0
                : this.ship.getCurrentFuel() / this.ship.getMaxFuel());

        return score;
    }

    public String getId()
    {
        return this.id;
    }

    public String getDifficulty()
    {
        return this.difficulty;
    }

    @Subscribe
    public void countdownFinished(CountdownFinishedEvent countdownFinishedEvent)
    {
        if (this.gameState == GameState.COUNTDOWN)
        {
            this.gameState = GameState.PLAY;
            if (!this.elapsedPlayTime.isRunning())
            {
                this.elapsedPlayTime.start();
            }
            SoundManager.resumeGameSound();
        }
    }

    public void dispose()
    {
        Constants.General.EVENT_BUS.unregister(this);

        this.pickupCollectedSound.stop();
        this.pickupCollectedSound.dispose();
    }

    public Ship getShip()
    {
        return this.ship;
    }

    public float getWidth()
    {
        return this.bounds.getWidth();
    }

    public float getHeight()
    {
        return this.bounds.getHeight();
    }

    public ArrayList<Pickup> getPickups()
    {
        return this.pickups;
    }

    public GameState getGameState()
    {
        return this.gameState;
    }

    public void pause()
    {
        if (this.gameState == GameState.PLAY
            || this.gameState == GameState.COUNTDOWN)
        {
            this.gameState = GameState.PAUSE;
            if (this.elapsedPlayTime.isRunning())
            {
                this.elapsedPlayTime.stop();
            }
            SoundManager.pauseGameSound();
        }
    }

    public void resume()
    {
        if (this.gameState == GameState.PAUSE)
        {
            ScreenManager.addScreen(new CountdownScreen());
            this.gameState = GameState.COUNTDOWN;
        }
    }

    public Projection processProjection(ArrayList<ProjectionPoint> projectionPoints)
    {
        Pool<Vertex> vertexPool = Pools.get(Vertex.class);
        ArrayList<Vertex> vertices = new ArrayList<>(projectionPoints.size());

        int lastCollisionIndex = 0;
        // Keep a list of PhysicsComponent that we already collided with to exclude them later on.
        ArrayList<PhysicsComponent> alreadyCollidedPhysicsComponents = new ArrayList<>();
        outerFor:
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
                Color currentColor = getColor(physicsComponentsToCheck);
                alreadyCollidedPhysicsComponents.addAll(physicsComponentsToCheck);

                // Add all the vertices up to this collision with the correct color.
                for (int j = lastCollisionIndex; j < i; j++)
                {
                    Vertex vertex = vertexPool.obtain();
                    vertex.set(currentColor, projectionPoints.get(j).getPosition().cpy());
                    vertices.add(j, vertex);
                }
                lastCollisionIndex = i;

                // If collided with a planet truncate here.
                for (PhysicsComponent physicsComponent : physicsComponentsToCheck)
                {
                    if (physicsComponent.getOwnerClass().equals(Planet.class))
                    {
                        break outerFor;
                    }
                }
            }
        }

        Pool<ProjectionPoint> projectionPointPool = Pools.get(ProjectionPoint.class);

        for (ProjectionPoint projectionPoint : projectionPoints)
        {
            projectionPointPool.free(projectionPoint);
        }


        Projection projection = Pools.obtain(Projection.class);
        projection.set(vertices);
        return projection;
    }

    private Color getColor(
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
                if (this.destinationPlanet != null
                    && this.destinationPlanet.physicsComponent.equals(nextCollidingPhysicsComponent))
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
        return this.destinationPlanet;
    }

    public void setDestinationPlanet(Planet destinationPlanet)
    {
        this.destinationPlanet = destinationPlanet;
    }

    public float getTrajectorySeconds()
    {
        return this.trajectorySeconds;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isTutorial()
    {
        return this.tutorial;
    }

    public void startTutorial()
    {
        this.gameState = GameState.TUTORIAL;
        SoundManager.pauseGameSound();
    }

    public void endTutorial()
    {
        ScreenManager.addScreen(new CountdownScreen());
        this.gameState = GameState.COUNTDOWN;
    }
}
