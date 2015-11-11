package com.draga.manager.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;

public class AssMan
{
    private static final AssetManager ASSET_MANAGER = new AssetManager();
    private static AssList ASSET_LIST;

    public static AssetManager getAssMan()
    {
        return ASSET_MANAGER;
    }

    public static AssList getAssList()
    {
        if (ASSET_LIST == null)
        {
            Json json = new Json();

            ASSET_LIST =
                json.fromJson(AssList.class, Gdx.files.internal("assetList.json").readString());
        }

        return ASSET_LIST;
    }
}
