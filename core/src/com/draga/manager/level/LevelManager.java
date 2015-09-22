package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.GameWorld;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;
import com.draga.planet.Planet;
import com.draga.ship.Ship;

/**
 * Created by Administrator on 05/09/2015.
 */
public abstract class LevelManager {
    public static SerialisableWorld getSerialisedWorldFromFile(String serialisedWorldFilePath) {
        String serialisedWordString = getStringFromFile(serialisedWorldFilePath);
        SerialisableWorld serialisableWorld = getSerialisedWorldFromString(serialisedWordString);

        return serialisableWorld;
    }

    private static String getStringFromFile(String filePath) {
        FileHandle serialisedWorldFileHandle = Gdx.files.internal(filePath);
        return serialisedWorldFileHandle.readString();
    }

    public static SerialisableWorld getSerialisedWorldFromString(String serialisedWord) {
        Json json = new Json();

        json.addClassTag("SerialisableWorld", SerialisableWorld.class);

        SerialisableWorld serialisableWorld =
            json.fromJson(SerialisableWorld.class, serialisedWord);

        return serialisableWorld;
    }

    public static GameWorld getLevelWorldFromFile(
        String serialisedWorldFilePath, SpriteBatch spriteBatch) {
        String serialisedWordString = getStringFromFile(serialisedWorldFilePath);
        GameWorld world = getLevelWorldFromString(serialisedWordString, spriteBatch);

        return world;
    }

    public static GameWorld getLevelWorldFromString(String jsonString, SpriteBatch spriteBatch) {
        SerialisableWorld serialisableWorld = LevelManager.getSerialisedWorldFromString(jsonString);

        GameWorld world = LevelManager.getLevelWorld(serialisableWorld, spriteBatch);

        return world;
    }

    private static GameWorld getLevelWorld(
        SerialisableWorld serialisableWorld, SpriteBatch spriteBatch) {
        GameWorld world = new GameWorld(
            serialisableWorld.serialisedBackground.getTexturePath(),
            spriteBatch,
            serialisableWorld.width,
            serialisableWorld.height);

        Ship ship = new Ship(
            serialisableWorld.serialisedShip.getTexturePath(),
            serialisableWorld.serialisedShip.getX(),
            serialisableWorld.serialisedShip.getY());
        world.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableWorld.serialisedPlanets) {
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
