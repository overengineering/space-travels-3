package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.entity.planet.Planet;
import com.draga.entity.ship.Ship;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;
import com.draga.manager.scene.GameScene;

/**
 * Created by Administrator on 05/09/2015.
 */
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

    public static GameScene getLevelWorldFromFile(
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
        GameScene world = new GameScene(
            serialisableWorld.serialisedBackground.getTexturePath(),
            spriteBatch,
            serialisableWorld.width,
            serialisableWorld.height);

        Ship ship = new Ship(
            serialisableWorld.serialisedShip.getTexturePath(),
            serialisableWorld.serialisedShip.getX(),
            serialisableWorld.serialisedShip.getY());
        world.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableWorld.serialisedPlanets)
        {
            Planet planet = new Planet(
                serialisablePlanet.getMass(),
                serialisablePlanet.getRadius(),
                serialisablePlanet.getX(),
                serialisablePlanet.getY(),
                serialisablePlanet.getTexturePath());
            world.addPlanet(planet);
        }

        return world;
    }
}
