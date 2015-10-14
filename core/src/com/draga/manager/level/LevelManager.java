package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.manager.level.serialisableEntities.SerialisableGameScene;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.manager.screen.GameScreen;

public abstract class LevelManager
{
    public static SerialisableGameScene getSerialisedGameSceneFromFile(String serialisedGameSceneFilePath)
    {
        String serialisedWordString = getStringFromFile(serialisedGameSceneFilePath);
        SerialisableGameScene serialisableGameScene =
            getSerialisedGameSceneFromString(serialisedWordString);

        return serialisableGameScene;
    }

    private static String getStringFromFile(String filePath)
    {
        FileHandle serialisedWorldFileHandle = Gdx.files.internal(filePath);
        return serialisedWorldFileHandle.readString();
    }

    public static SerialisableGameScene getSerialisedGameSceneFromString(String serialisedWord)
    {
        Json json = new Json();

        json.addClassTag("SerialisableGameScene", SerialisableGameScene.class);

        SerialisableGameScene serialisableGameScene =
            json.fromJson(SerialisableGameScene.class, serialisedWord);

        return serialisableGameScene;
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
        SerialisableGameScene serialisableGameScene = LevelManager.getSerialisedGameSceneFromString(
            jsonString);

        GameScreen world = LevelManager.getLevelGameScene(serialisableGameScene, spriteBatch);

        return world;
    }

    public static GameScreen getLevelGameScene(
        SerialisableGameScene serialisableGameScene, SpriteBatch spriteBatch)
    {
        GameScreen gameScreen = new GameScreen(
            serialisableGameScene.serialisedBackground.getTexturePath(),
            spriteBatch,
            serialisableGameScene.width,
            serialisableGameScene.height);

        Ship ship = new Ship(
            serialisableGameScene.serialisedShip.getX(),
            serialisableGameScene.serialisedShip.getY(),
            serialisableGameScene.serialisedShip.getShipTexturePath(),
            serialisableGameScene.serialisedShip.getThrusterTextureAtlasPath());
        gameScreen.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableGameScene.serialisedPlanets)
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
