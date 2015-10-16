package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.entity.Star;
import com.draga.manager.level.serialisableEntities.SerialisableLevel;
import com.draga.manager.level.serialisableEntities.SerialisablePlanet;
import com.draga.manager.level.serialisableEntities.SerialisableStar;
import com.draga.manager.screen.GameScreen;

public abstract class LevelManager
{
    public static SerialisableLevel getSerialisedGameSceneFromFile(String serialisedGameSceneFilePath)
    {
        String serialisedWordString = getStringFromFile(serialisedGameSceneFilePath);
        SerialisableLevel serialisableLevel =
            getSerialisedGameSceneFromString(serialisedWordString);

        return serialisableLevel;
    }

    private static String getStringFromFile(String filePath)
    {
        FileHandle serialisedWorldFileHandle = Gdx.files.internal(filePath);
        return serialisedWorldFileHandle.readString();
    }

    public static SerialisableLevel getSerialisedGameSceneFromString(String serialisedWord)
    {
        Json json = new Json();

        json.addClassTag("SerialisableLevel", SerialisableLevel.class);

        SerialisableLevel serialisableLevel =
            json.fromJson(SerialisableLevel.class, serialisedWord);

        return serialisableLevel;
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
        SerialisableLevel serialisableLevel = LevelManager.getSerialisedGameSceneFromString(
            jsonString);

        GameScreen world = LevelManager.getLevelGameScene(serialisableLevel, spriteBatch);

        return world;
    }

    public static GameScreen getLevelGameScene(
        SerialisableLevel serialisableLevel, SpriteBatch spriteBatch)
    {
        GameScreen gameScreen = new GameScreen(
            serialisableLevel.serialisedBackground.getTexturePath(),
            spriteBatch,
            serialisableLevel.width,
            serialisableLevel.height);

        Ship ship = new Ship(
            serialisableLevel.serialisedShip.getX(),
            serialisableLevel.serialisedShip.getY(),
            serialisableLevel.serialisedShip.getShipTexturePath(),
            serialisableLevel.serialisedShip.getThrusterTextureAtlasPath());
        gameScreen.addShip(ship);

        for (SerialisablePlanet serialisablePlanet : serialisableLevel.serialisedPlanets)
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

        for (SerialisableStar serialisableStar : serialisableLevel.serialisedStars)
        {
            Star star = new Star(serialisableStar.getX(), serialisableStar.getY(), "star/starGold64.png");
            gameScreen.addStar(star);
        }

        return gameScreen;
    }
}
