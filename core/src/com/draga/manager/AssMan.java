package com.draga.manager;

import com.badlogic.gdx.assets.AssetManager;

public class AssMan
{
    private static final AssetManager ASSET_MANAGER = new AssetManager();

    public static AssetManager getAssetManager()
    {
        return ASSET_MANAGER;
    }
}
