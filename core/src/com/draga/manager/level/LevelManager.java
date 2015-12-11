package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.draga.Level;
import com.draga.gameEntity.Pickup;
import com.draga.gameEntity.Planet;
import com.draga.gameEntity.Ship;
import com.draga.gameEntity.Thruster;
import com.draga.manager.GameEntityManager;
import com.draga.manager.asset.AssMan;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.manager.level.serialisableEntities.SerialisablePickup;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LevelManager
{
    private static final String LOGGING_TAG = LevelManager.class.getSimpleName();
    private static ArrayList<SerialisableLevel> levels;

    public static Level getLevel(SerialisableLevel serialisableLevel)
    {/*
        GameScreen gameScreen = new GameScreen(
            serialisableLevel.serialisedBackground.texturePath,
            serialisableLevel.width,
            serialisableLevel.height,
            serialisableLevel.name);*/

        Ship ship = new Ship(
            serialisableLevel.serialisedShip.x,
            serialisableLevel.serialisedShip.y,
            serialisableLevel.serialisedShip.mass,
            AssMan.getAssList().shipTexture,
            serialisableLevel.fuel);
        //        gameScreen.addShip(ship);
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
            //            gameScreen.addPlanet(planet);
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
            //            gameScreen.addPickup(pickup);
        }

        Thruster thruster = new Thruster(ship);
        Level level = new Level(
            serialisableLevel.name,
            ship,
            thruster,
            planets,
            pickups,
            destinationPlanet,
            serialisableLevel.width,
            serialisableLevel.height);
        // Run one update so everything is in place for the countdown
        GameEntityManager.update();
        return level;
    }

    public static SerialisableLevel getLevel(String levelName)
    {
        Iterator<SerialisableLevel> serialisableLevelIterator = LevelManager.getLevels().iterator();

        while (serialisableLevelIterator.hasNext())
        {
            SerialisableLevel serialisableLevel = serialisableLevelIterator.next();
            if (serialisableLevel.name.equals(levelName))
            {
                return serialisableLevel;
            }
        }

        Gdx.app.error(LOGGING_TAG, "Could not find a level with name \"" + levelName + "\"");
        return null;
    }

    public static List<SerialisableLevel> getLevels()
    {
        if (levels == null)
        {
            loadLevels();
        }

        return levels;
    }

    /**
     * Load the levels from the level pack file in order
     */
    private static void loadLevels()
    {
        Json json = new Json();
        String levelPackString = Gdx.files.internal("level/levelPack.json").readString();
        ArrayList<String> levelFileNameWithExtension = json.fromJson(
            ArrayList.class,
            levelPackString);
        levels = new ArrayList<>();
        json.addClassTag("SerialisableLevel", SerialisableLevel.class);
        for (String levelNameWithExtension : levelFileNameWithExtension)
        {
            String levelString = Gdx.files.internal("level/" + levelNameWithExtension).readString();
            SerialisableLevel serialisableLevel =
                json.fromJson(SerialisableLevel.class, levelString);
            levels.add(serialisableLevel);
        }
    }

    /**
     * Get the serialisable level immediately after the level specified as a parameter.
     * Null if it was the last level.
     */
    public static SerialisableLevel getNextLevel(String levelName)
    {
        Iterator<SerialisableLevel> serialisableLevelIterator = LevelManager.getLevels().iterator();

        while (serialisableLevelIterator.hasNext())
        {
            SerialisableLevel serialisableLevel = serialisableLevelIterator.next();
            if (serialisableLevel.name.equals(levelName))
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

        Gdx.app.error(LOGGING_TAG, "Could not find a level with name \"" + levelName + "\"");
        return null;
    }
}
