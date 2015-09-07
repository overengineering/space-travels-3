package com.draga.manager.level;

import com.badlogic.gdx.utils.Json;
import com.draga.manager.level.serialisableEntities.SerialisableWorld;

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
