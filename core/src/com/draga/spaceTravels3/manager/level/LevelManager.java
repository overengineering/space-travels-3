package com.draga.spaceTravels3.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.draga.spaceTravels3.Level;
import com.draga.spaceTravels3.gameEntity.Pickup;
import com.draga.spaceTravels3.gameEntity.Planet;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.gameEntity.Thruster;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisablePickup;
import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisablePlanet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LevelManager
{
    private static final String LOGGING_TAG = LevelManager.class.getSimpleName();
    private static ArrayList<SerialisableLevel> serialisableLevels;

    public static Level getLevel(SerialisableLevel serialisableLevel)
    {
        Ship ship = new Ship(
            serialisableLevel.serialisedShip.x,
            serialisableLevel.serialisedShip.y,
            serialisableLevel.serialisedShip.mass,
            AssMan.getAssList().shipTexture,
            serialisableLevel.fuel);

        ArrayList<Planet> planets = new ArrayList<>(serialisableLevel.serialisedPlanets.size());
        Planet destinationPlanet = null;

        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            Planet planet = new Planet(
                serialisablePlanet.mass,
                serialisablePlanet.radius,
                serialisablePlanet.x,
                serialisablePlanet.y,
                serialisablePlanet.texturePath,
                serialisablePlanet.destination);

            planets.add(planet);
            if (serialisablePlanet.destination)
            {
                destinationPlanet = planet;
            }
        }

        ArrayList<Pickup> pickups = new ArrayList<>(serialisableLevel.serialisedPickups.size());
        for (SerialisablePickup serialisablePickup : serialisableLevel.serialisedPickups)
        {
            Pickup pickup =
                new Pickup(
                    serialisablePickup.x,
                    serialisablePickup.y,
                    AssMan.getAssList().pickupTexture);
            pickups.add(pickup);
        }

        Thruster thruster = new Thruster(ship);

        Level level = new Level(
            serialisableLevel.id,
            serialisableLevel.name,
            ship,
            thruster,
            planets,
            pickups,
            destinationPlanet,
            serialisableLevel.width,
            serialisableLevel.height,
            serialisableLevel.trajectorySeconds,
            serialisableLevel.maxLandingSpeed);

        // Run one update so everything is in place for the countdown
        GameEntityManager.update();

        return level;
    }

    public static SerialisableLevel getSerialisableLevel(String levelId)
    {
        Iterator<SerialisableLevel> serialisableLevelIterator =
            LevelManager.getSerialisableLevels().iterator();

        while (serialisableLevelIterator.hasNext())
        {
            SerialisableLevel serialisableLevel = serialisableLevelIterator.next();
            if (serialisableLevel.id.equals(levelId))
            {
                return serialisableLevel;
            }
        }

        Gdx.app.error(LOGGING_TAG, "Could not find a level with name \"" + levelId + "\"");
        return null;
    }

    public static List<SerialisableLevel> getSerialisableLevels()
    {
        if (serialisableLevels == null)
        {
            loadSerialisableLevels();
        }

        return serialisableLevels;
    }

    /**
     * Load the serialisableLevels from the level pack file in order
     */
    private static void loadSerialisableLevels()
    {
        Json json = new Json();
        String levelPackString = Gdx.files.internal("level/levelPack.json").readString();
        ArrayList<String> levelFileNamesWithExtension = json.fromJson(
            ArrayList.class,
            levelPackString);
        serialisableLevels = new ArrayList<>();
        json.addClassTag("SerialisableLevel", SerialisableLevel.class);
        for (String levelFileNameWithExtension : levelFileNamesWithExtension)
        {
            String levelString = Gdx.files.internal("level/" + levelFileNameWithExtension).readString();
            SerialisableLevel serialisableLevel =
                json.fromJson(SerialisableLevel.class, levelString);

            serialisableLevel.id = levelFileNameWithExtension;

            serialisableLevels.add(serialisableLevel);
        }
    }

    /**
     * Get the serialisable level immediately after the level specified as a parameter.
     * Null if it was the last level.
     */
    public static SerialisableLevel getNextLevel(String levelId)
    {
        Iterator<SerialisableLevel> serialisableLevelIterator =
            LevelManager.getSerialisableLevels().iterator();

        while (serialisableLevelIterator.hasNext())
        {
            SerialisableLevel serialisableLevel = serialisableLevelIterator.next();
            if (serialisableLevel.id.equals(levelId))
            {
                if (serialisableLevelIterator.hasNext())
                {
                    return serialisableLevelIterator.next();
                }
                else
                {
                    return null;
                }
            }
        }

        Gdx.app.error(LOGGING_TAG, "Could not find a level with name \"" + levelId + "\"");
        return null;
    }
}
