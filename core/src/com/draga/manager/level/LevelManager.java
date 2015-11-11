package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.Constants;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.entity.Star;
import com.draga.manager.asset.AssMan;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.manager.level.serialisableEntities.SerialisableStar;
import com.draga.manager.screen.GameScreen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LevelManager
{
    private static final String LOGGING_TAG = LevelManager.class.getSimpleName();
    private static ArrayList<SerialisableLevel> levels;

    public static GameScreen getLevelGameScreen(
        SerialisableLevel serialisableLevel, SpriteBatch spriteBatch)
    {
        GameScreen gameScreen = new GameScreen(
            serialisableLevel.serialisedBackground.texturePath,
            spriteBatch,
            serialisableLevel.width,
            serialisableLevel.height,
            serialisableLevel.name);

        Ship ship = new Ship(
            serialisableLevel.serialisedShip.x,
            serialisableLevel.serialisedShip.y,
            AssMan.getAssList().ship,
            AssMan.getAssList().thruster);
        gameScreen.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            Planet planet = new Planet(
                serialisablePlanet.mass,
                serialisablePlanet.radius,
                serialisablePlanet.x,
                serialisablePlanet.y,
                serialisablePlanet.texturePath);

            gameScreen.addPlanet(planet);

            if (serialisablePlanet.destination)
            {
                gameScreen.setDestinationPlanet(planet);
            }
        }

        for (SerialisableStar serialisableStar : serialisableLevel.serialisedStars)
        {
            Star star =
                new Star(serialisableStar.x, serialisableStar.y, AssMan.getAssList().starGold);
            gameScreen.addStar(star);
        }

        return gameScreen;
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

    /** Load the levels from the level pack file in order */
    private static void loadLevels()
    {
        long startTime = System.nanoTime();

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

        long elapsedNanoTime = System.nanoTime() - startTime;
        Gdx.app.debug(LOGGING_TAG, "Time to read the level pack serialised levels: " + elapsedNanoTime * Constants.NANO + "s");
    }

    /** Get the serialisable level immediately after the level specified as a parameter.
     * Null if it was the last level. */
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
