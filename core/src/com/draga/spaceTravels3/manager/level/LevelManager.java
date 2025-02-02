package com.draga.spaceTravels3.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.gameEntity.Pickup;
import com.draga.spaceTravels3.gameEntity.Planet;
import com.draga.spaceTravels3.gameEntity.Ship;
import com.draga.spaceTravels3.gameEntity.Thruster;
import com.draga.spaceTravels3.level.Level;
import com.draga.spaceTravels3.manager.GameEntityManager;
import com.draga.spaceTravels3.manager.asset.AssMan;
import com.draga.spaceTravels3.manager.level.serialisableEntities.*;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class LevelManager
{
    private static final String LOGGING_TAG = LevelManager.class.getSimpleName();

    private static ArrayList<LevelPack> levelPacks = loadLevelPacks();
    private static SerialisableLevel tutorialSerialisableLevel;

    public static ArrayList<LevelPack> getLevelPacks()
    {
        return levelPacks;
    }

    public static Level getLevel(
        SerialisableLevel serialisableLevel,
        String difficulty, boolean tutorial)
    {
        AssetManager gameAssMan = AssMan.getGameAssMan();
        AssetManager assMan = AssMan.getAssMan();

        SerialisableDifficulty serialisableDifficulty =
            serialisableLevel.serialisedDifficulties.get(difficulty);

        Ship ship = new Ship(
            serialisableLevel.serialisedShip.x,
            serialisableLevel.serialisedShip.y,
            serialisableLevel.serialisedShip.mass,
            gameAssMan.get(AssMan.getAssList().shipTexture, Texture.class),
            serialisableDifficulty.fuel,
            serialisableDifficulty.infiniteFuel);

        ArrayList<Planet> planets = new ArrayList<>(serialisableLevel.serialisedPlanets.size());

        Planet destinationPlanet = null;
        if (serialisableLevel.serialisedDestinationPlanet != null)
        {
            destinationPlanet = getPlanetFromSerialisablePlanet(
                serialisableLevel.serialisedDestinationPlanet,
                assMan,
                true);
            planets.add(destinationPlanet);
        }

        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
        {
            Planet planet = getPlanetFromSerialisablePlanet(serialisablePlanet, gameAssMan, false);

            planets.add(planet);
        }

        ArrayList<Pickup> pickups = new ArrayList<>(serialisableLevel.serialisedPickups.size());
        for (SerialisablePickup serialisablePickup : serialisableLevel.serialisedPickups)
        {
            Pickup pickup =
                new Pickup(
                    serialisablePickup.x,
                    serialisablePickup.y,

                    gameAssMan.get(AssMan.getAssList().pickupTexture, Texture.class));
            pickups.add(pickup);
        }

        Thruster thruster = new Thruster(
            ship,
            gameAssMan.get(AssMan.getAssList().thrusterTextureAtlas, TextureAtlas.class));

        Level level = new Level(
            serialisableLevel.id,
            serialisableLevel.name,
            difficulty,
            ship,
            thruster,
            planets,
            pickups,
            destinationPlanet,
            serialisableDifficulty.trajectorySeconds,
            serialisableDifficulty.maxLandingSpeed,
            tutorial,
            serialisableDifficulty.playCompletionAchievementID,
            serialisableDifficulty.playLeaderboardID);

        // Run one update so everything is in place for the countdown
        GameEntityManager.update();

        return level;
    }

    private static Planet getPlanetFromSerialisablePlanet(
        SerialisablePlanet serialisablePlanet,
        AssetManager assMan,
        boolean isDestination)
    {
        return new Planet(
            serialisablePlanet.mass,
            serialisablePlanet.radius,
            serialisablePlanet.x,
            serialisablePlanet.y,
            assMan.get(serialisablePlanet.texturePath, Texture.class),
            isDestination);
    }

    public static SerialisableLevel getNextSerialisableLevel(String levelId)
    {
        for (LevelPack levelPack : levelPacks)
        {
            for (Iterator<SerialisableLevel> iterator =
                 levelPack.getSerialisableLevels().iterator(); iterator.hasNext(); )
            {
                SerialisableLevel serialisableLevel = iterator.next();
                if (serialisableLevel.id.equals(levelId))
                {
                    return iterator.hasNext()
                        ? iterator.next()
                        : null;
                }
            }
        }

        ErrorHandlerProvider.handle(
            LOGGING_TAG,
            "Could not find a level with name \"" + levelId + "\"");

        return null;
    }

    private static ArrayList<LevelPack> loadLevelPacks()
    {
        Json json = new Json();

        String levelPacksString = Gdx.files.internal("level/levelPacks.json").readString();

        @SuppressWarnings("unchecked")
        ArrayList<String> serialisableLevelPackPaths = json.fromJson(
            ArrayList.class,
            String.class,
            levelPacksString);

        ArrayList<LevelPack> levelPacks = new ArrayList<>(serialisableLevelPackPaths.size());

        for (String serialisableLevelPackPath : serialisableLevelPackPaths)
        {
            LevelPack levelPack = loadLevelPack(serialisableLevelPackPath);
            levelPacks.add(levelPack);
        }

        return levelPacks;
    }

    private static LevelPack loadLevelPack(String serialisableLevelPackPath)
    {
        Json json = new Json();
        json.addClassTag("SerialisableLevel", SerialisableLevel.class);


        FileHandle serialisableLevelPackFileHandle =
            Gdx.files.internal("level/" + serialisableLevelPackPath);
        FileHandle levelPackRoot = serialisableLevelPackFileHandle.parent();

        String serialisableLevelPackString = serialisableLevelPackFileHandle.readString();

        SerialisableLevelPack serialisableLevelPack = json.fromJson(
            SerialisableLevelPack.class,
            serialisableLevelPackString);

        LevelPack levelPack = new LevelPack(
            serialisableLevelPack.name,
            serialisableLevelPack.free,
            serialisableLevelPack.googleSku);

        for (String serialisableLevelPath : serialisableLevelPack.serialisableLevelPaths)
        {
            String serialisableLevelString =
                levelPackRoot.child(serialisableLevelPath).readString();
            SerialisableLevel serialisableLevel =
                json.fromJson(SerialisableLevel.class, serialisableLevelString);

            serialisableLevel.id = serialisableLevelPack.name + "/" + serialisableLevelPath;

            levelPack.getSerialisableLevels().add(serialisableLevel);
        }

        return levelPack;
    }

    public static SerialisableLevel getTutorialSerialisableLevel()
    {
        if (tutorialSerialisableLevel == null)
        {
            Json json = new Json();
            json.addClassTag("SerialisableLevel", SerialisableLevel.class);
            String tutorialSerialisableLevelString =
                Gdx.files.internal("tutorial/level.json").readString();

            tutorialSerialisableLevel =
                json.fromJson(SerialisableLevel.class, tutorialSerialisableLevelString);
        }

        return tutorialSerialisableLevel;
    }

    public static String getNextDifficulty(String levelID, String difficulty)
    {
        SerialisableLevel serialisableLevel = getSerialisableLevel(levelID);
        if (serialisableLevel != null)
        {
            for (Iterator<String> iterator =
                 serialisableLevel.serialisedDifficulties.keySet().iterator(); iterator.hasNext(); )
            {
                String difficultyKey = iterator.next();
                if (difficultyKey.equals(difficulty))
                {
                    return iterator.hasNext()
                        ? iterator.next()
                        : null;
                }
            }
        }

        return null;
    }

    public static SerialisableLevel getSerialisableLevel(String levelId)
    {
        for (LevelPack levelPack : levelPacks)
        {
            for (SerialisableLevel serialisableLevel : levelPack.getSerialisableLevels())
            {
                if (serialisableLevel.id.equals(levelId))
                {
                    return serialisableLevel;
                }
            }
        }

        ErrorHandlerProvider.handle(
            LOGGING_TAG,
            "Could not find a level with name \"" + levelId + "\"");

        return null;
    }

    public static LevelPack getNextLevelPack(String levelID)
    {
        for (Iterator<LevelPack> levelPackIterator =
             levelPacks.iterator(); levelPackIterator.hasNext(); )
        {
            LevelPack levelPack = levelPackIterator.next();
            for (SerialisableLevel serialisableLevel : levelPack.getSerialisableLevels())
            {
                if (serialisableLevel.id.equals(levelID))
                {
                    return levelPackIterator.hasNext()
                        ? levelPackIterator.next()
                        : null;
                }
            }
        }

        return null;
    }
}
