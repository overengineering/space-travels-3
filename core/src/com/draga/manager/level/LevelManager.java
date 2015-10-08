package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;
import com.draga.manager.screen.GameScreen;

public abstract class LevelManager
{
    public static SerialisableWorld getSerialisedGameSceneFromFile(String serialisedWorldFilePath)
    {
        String serialisedWordString = getStringFromFile(serialisedWorldFilePath);
        SerialisableWorld serialisableWorld =
            getSerialisedGameSceneFromString(serialisedWordString);

        return serialisableWorld;
    }

    private static String getStringFromFile(String filePath)
    {
        FileHandle serialisedWorldFileHandle = Gdx.files.internal(filePath);
        return serialisedWorldFileHandle.readString();
    }

    public static SerialisableWorld getSerialisedGameSceneFromString(String serialisedWord)
    {
        Json json = new Json();

        json.addClassTag("SerialisableWorld", SerialisableWorld.class);

        SerialisableWorld serialisableWorld =
            json.fromJson(SerialisableWorld.class, serialisedWord);

        return serialisableWorld;
    }

    public static GameScreen getLevelWorldFromFile(
        String serialisedWorldFilePath, SpriteBatch spriteBatch)
    {
        String serialisedWordString = getStringFromFile(serialisedWorldFilePath);
        GameScreen world = getLevelGameSceneFromString(serialisedWordString, spriteBatch);

        return world;
    }

    public static GameScreen getLevelGameSceneFromString(String jsonString, SpriteBatch spriteBatch)
    {
        SerialisableWorld serialisableWorld = LevelManager.getSerialisedGameSceneFromString(
            jsonString);

        GameScreen world = LevelManager.getLevelGameScene(serialisableWorld, spriteBatch);

        return world;
    }

    private static GameScreen getLevelGameScene(
        SerialisableWorld serialisableWorld, SpriteBatch spriteBatch)
    {
        GameScreen gameScreen = new GameScreen(
            serialisableWorld.serialisedBackground.getTexturePath(),
            spriteBatch,
            serialisableWorld.width,
            serialisableWorld.height);

        Ship ship = new Ship(
            serialisableWorld.serialisedShip.getX(),
            serialisableWorld.serialisedShip.getY(),
            serialisableWorld.serialisedShip.getTexturePath(),
            "thruster/thrusterSize256Frames75.txt");
        gameScreen.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableWorld.serialisedPlanets)
        {
            Planet planet = new Planet(
                serialisablePlanet.getMass(),
                serialisablePlanet.getRadius(),
                serialisablePlanet.getX(),
                serialisablePlanet.getY(),
                serialisablePlanet.getTexturePath());
            gameScreen.addPlanet(planet);

            if (serialisablePlanet.isDestination())
            {
                gameScreen.setDestinationPlanet(planet);
            }
        }

        return gameScreen;
    }
}
