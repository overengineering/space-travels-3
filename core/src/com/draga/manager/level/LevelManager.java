package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
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
    public static SerialisableLevel getSerialisedLevelFromName(String serialisedLevelName)
    {
        String serialisedLevelString =
            Gdx.files.internal(serialisedLevelName).readString();
        SerialisableLevel serialisableLevel = getSerialisedLevelFromString(serialisedLevelString);

        return serialisableLevel;
    }

    public static GameScreen getLevelGameScreen(
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
            Star star =
                new Star(serialisableStar.getX(), serialisableStar.getY(), "star/starGold64.png");
            gameScreen.addStar(star);
        }

        return gameScreen;
    }

    private static SerialisableLevel getSerialisedLevelFromString(String serialisedLevel)
    {
        Json json = new Json();

        json.addClassTag("SerialisableLevel", SerialisableLevel.class);

        SerialisableLevel serialisableLevel =
            json.fromJson(SerialisableLevel.class, serialisedLevel);

        return serialisableLevel;
    }

    private static GameScreen getLevelGameScreenFromString(
        String jsonString, SpriteBatch spriteBatch)
    {
        SerialisableLevel serialisableLevel = LevelManager.getSerialisedLevelFromString(
            jsonString);

        GameScreen gameScreen = LevelManager.getLevelGameScreen(serialisableLevel, spriteBatch);

        return gameScreen;
    }
}
