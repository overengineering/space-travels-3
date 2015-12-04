package com.draga.manager.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Json;

public class AssMan
{
    private static AssetManager ASSET_MANAGER;
    private static AssList      ASSET_LIST;

    public static void create()
    {
        ASSET_MANAGER = new AssetManager();
    }

    public static void dispose()
    {
        ASSET_MANAGER.dispose();
    }

    public static AssetManager getAssMan()
    {
        return ASSET_MANAGER;
    }

    public static AssList getAssList()
    {
        // TODO: move to create? maybe a bit overkill but to deserialise these small object is very
        // fast
        if (ASSET_LIST == null)
        {
            Json json = new Json();

            ASSET_LIST =
                json.fromJson(AssList.class, Gdx.files.internal("assetList.json").readString());
        }

        return ASSET_LIST;
    }
}
