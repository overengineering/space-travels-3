package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.manager.level.serialisableEntities.SerialisableGameScene;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.scene.GameScene;

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

    public static GameScene getGameSceneFromFile(
        String serialisedGameSceneFilePath,
        SpriteBatch spriteBatch)
    {
        String serialisedWordString = getStringFromFile(serialisedGameSceneFilePath);
        GameScene gameScene = getLevelGameSceneFromString(serialisedWordString, spriteBatch);

        return gameScene;
    }

    public static GameScene getLevelGameSceneFromString(String jsonString, SpriteBatch spriteBatch)
    {
        SerialisableGameScene serialisableGameScene = LevelManager.getSerialisedGameSceneFromString(
            jsonString);

        GameScene gameScene = LevelManager.getLevelGameScene(serialisableGameScene, spriteBatch);

        return gameScene;
    }

    public static GameScene getLevelGameScene(
        SerialisableGameScene serialisableGameScene, SpriteBatch spriteBatch)
    {
        GameScene gameScene = new GameScene(
            serialisableGameScene.serialisedBackground.getTexturePath(),
            spriteBatch,
            serialisableGameScene.width,
            serialisableGameScene.height);

        Ship ship = new Ship(
            serialisableGameScene.serialisedShip.getX(),
            serialisableGameScene.serialisedShip.getY(),
            serialisableGameScene.serialisedShip.getShipTexturePath(),
            serialisableGameScene.serialisedShip.getThrusterTextureAtlasPath());
        gameScene.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableGameScene.serialisedPlanets)
        {
            Planet planet = new Planet(
                serialisablePlanet.getMass(),
                serialisablePlanet.getRadius(),
                serialisablePlanet.getX(),
                serialisablePlanet.getY(),
                serialisablePlanet.getTexturePath());
            gameScene.addPlanet(planet);
        }

        return gameScene;
    }
}
