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
    public static SerialisableWorld getSerialisedWord(String serialisedWord)
    {
        Json json = new Json();

        json.addClassTag("SerialisableWorld", SerialisableWorld.class);

        SerialisableWorld serialisableWorld = json.fromJson(SerialisableWorld.class, serialisedWord);

        return serialisableWorld;
    }
}
