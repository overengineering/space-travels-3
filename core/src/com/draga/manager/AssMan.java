package com.draga.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class AssMan
{
    private static final AssetManager ASSET_MANAGER = new AssetManager();

    public static AssetManager getAssetManager()
    {
        return ASSET_MANAGER;
    }

    public static void DisposeAllAndClear()
    {
        Array<Disposable> allAssets = new Array<>();
        ASSET_MANAGER.getAll(Disposable.class, allAssets);

        for (Disposable asset : allAssets)
        {
            asset.dispose();
        }

        ASSET_MANAGER.clear();
    }
}
