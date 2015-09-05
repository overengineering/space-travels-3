package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.draga.manager.serialisableEntities.SerialisableBackground;
import com.draga.manager.serialisableEntities.SerialisablePlanet;
import com.draga.manager.serialisableEntities.SerialisableShip;
import com.draga.manager.serialisableEntities.SerialisableWorld;

/**
 * Created by Administrator on 05/09/2015.
 */
public class LevelManager
{
    public static SerialisableWorld getSerialisedWord(String levelPath)
    {
        Json json = new Json();
        json.addClassTag("World", SerialisableWorld.class);
        json.addClassTag("Background", SerialisableBackground.class);
        json.addClassTag("Planet", SerialisablePlanet.class);
        json.addClassTag("Ship", SerialisableShip.class);

        FileHandle fileHandle = Gdx.files.internal(levelPath);
        SerialisableWorld serialisableWorld = json.fromJson(
            SerialisableWorld.class, fileHandle);

        return serialisableWorld;
    }
}
