package com.draga;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Pools;
import com.draga.event.*;
import com.draga.gameEntity.*;
import com.draga.manager.GameEntityManager;
import com.draga.manager.SettingsManager;
import com.draga.manager.asset.AssMan;
import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Level
{
    private static final String LOGGING_TAG = Level.class.getSimpleName();

    private final float width;
    private final float height;

    private final ArrayList<Pickup> pickups;
    private final Ship              ship;
    private final Thruster          thruster;
    private final Planet            destinationPlanet;
    private       int               pickupsCollected;

    private GameState gameState;
    private Stopwatch elapsedPlayTime = Stopwatch.createUnstarted();

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
        float width,
        float height)
    {
        this.ship = ship;
        this.thruster = thruster;
        this.destinationPlanet = destinationPlanet;
        this.pickups = pickups;
        this.width = width;
        this.height = height;
        this.id = id;
        this.name = name;

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

        Constants.General.EVENT_BUS.register(this);
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

        elapsedPlayTime.stop();

        // If wrong planet or too fast then lose.
        if (!(shipPlanetCollisionEvent.planet.equals(destinationPlanet))
            || ship.physicsComponent.getVelocity().len()
            > Constants.Game.MAX_DESTINATION_PLANET_APPROACH_SPEED)
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
        // TODO: Max fuel can be all over the place. Percentage of remaining?
        float fuelPoints = ship.getCurrentFuel() * Constants.Game.FUEL_POINTS;

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
        return width;
    }

    public float getHeight()
    {
        return height;
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
}
