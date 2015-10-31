package com.draga.manager.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.draga.entity.Planet;
import com.draga.entity.Ship;
import com.draga.entity.Star;
import com.draga.manager.asset.AssMan;
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
        SerialisableLevel serialisableLevel =
            getSerialisedLevelFromString(serialisedLevelString, serialisedLevelName);

        return serialisableLevel;
    }

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

    private static SerialisableLevel getSerialisedLevelFromString(
        String serialisedLevel,
        String name)
    {
        Json json = new Json();

        json.addClassTag("SerialisableLevel", SerialisableLevel.class);

        SerialisableLevel serialisableLevel =
            json.fromJson(SerialisableLevel.class, serialisedLevel);
        serialisableLevel.name = name;

        return serialisableLevel;
    }
}
