package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;
import com.draga.scene.GameScene;

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

    public static GameScene getGameSceneFromFile(
        String serialisedWorldFilePath, SpriteBatch spriteBatch)
    {
        String serialisedWordString = getStringFromFile(serialisedWorldFilePath);
        GameScene world = getLevelGameSceneFromString(serialisedWordString, spriteBatch);

        return world;
    }

    public static GameScene getLevelGameSceneFromString(String jsonString, SpriteBatch spriteBatch)
    {
        SerialisableWorld serialisableWorld = LevelManager.getSerialisedGameSceneFromString(
            jsonString);

        GameScene world = LevelManager.getLevelGameScene(serialisableWorld, spriteBatch);

        return world;
    }

    private static GameScene getLevelGameScene(
        SerialisableWorld serialisableWorld, SpriteBatch spriteBatch)
    {
        AssetManager assetManager = new AssetManager();
        assetManager.load(serialisableWorld.serialisedBackground.getTexturePath(), Texture.class);
        assetManager.load(serialisableWorld.serialisedShip.getShipTexturePath(), Texture.class);
        assetManager.load(
            serialisableWorld.serialisedShip.getThrusterTextureAtlasPath(), TextureAtlas.class);
        for (SerialisablePlanet serialisablePlanet : serialisableWorld.serialisedPlanets)
        {
            assetManager.load(serialisablePlanet.getTexturePath(), Texture.class);
        }
        assetManager.load("explosion/explosion.atlas", TextureAtlas.class);


        Long startTime = System.nanoTime();
        assetManager.finishLoading();
        Long finishTime = System.nanoTime();
        Gdx.app.log("AssetManager", "Time: " + ((finishTime - startTime) * 0.000000001));


        GameScene gameScene = new GameScene(
            serialisableWorld.serialisedBackground.getTexturePath(),
            spriteBatch,
            serialisableWorld.width,
            serialisableWorld.height,
            assetManager);

        Ship ship = new Ship(
            serialisableWorld.serialisedShip.getX(),
            serialisableWorld.serialisedShip.getY(),
            serialisableWorld.serialisedShip.getShipTexturePath(),
            serialisableWorld.serialisedShip.getThrusterTextureAtlasPath(),
            assetManager);
        gameScene.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableWorld.serialisedPlanets)
        {
            Planet planet = new Planet(
                serialisablePlanet.getMass(),
                serialisablePlanet.getRadius(),
                serialisablePlanet.getX(),
                serialisablePlanet.getY(),
                serialisablePlanet.getTexturePath(),
                assetManager);
            gameScene.addPlanet(planet);
        }

        return gameScene;
    }
}
