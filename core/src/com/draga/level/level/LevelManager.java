package com.draga.level.level;

import com.badlogic.gdx.utils.Json;
import com.draga.GameWorld;
import com.draga.World;
import com.draga.level.level.serialisableEntities.SerialisablePlanet;
import com.draga.level.level.serialisableEntities.SerialisableWorld;
import com.draga.planet.Planet;
import com.draga.ship.Ship;

/**
 * Created by Administrator on 05/09/2015.
 */
public abstract class LevelManager
{
    public static SerialisableWorld getSerialisedWord(String serialisedWord)
    {
        Json json = new Json();

        json.addClassTag("SerialisableWorld", SerialisableWorld.class);

        SerialisableWorld serialisableWorld = json.fromJson(SerialisableWorld.class, serialisedWord);

        return serialisableWorld;
    }

    public static World getLevelWorld(String jsonString)
    {
        SerialisableWorld serialisableWorld = LevelManager.getSerialisedWord(jsonString);

        World world = LevelManager.getLevelWorld(serialisableWorld);

        return world;
    }

    private static World getLevelWorld(SerialisableWorld serialisableWorld)
    {
        World world = new GameWorld(serialisableWorld.serialisedBackground.getTexturePath());

        Ship ship = new Ship(serialisableWorld.serialisedShip.getTexturePath());
        world.addGameEntity(ship);

        for (SerialisablePlanet serialisablePlanet: serialisableWorld.serialisedPlanets)
        {
            Planet planet = new Planet(
                serialisablePlanet.getMass(),
                serialisablePlanet.getDiameter(),
                serialisablePlanet.getX(),
                serialisablePlanet.getY(),
                serialisablePlanet.getTexturePath()
            );
            world.addGameEntity(planet);
        }

        return world;
    }
}
