package com.draga.spaceTravels3.manager.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Json;
import com.draga.background.Background;
import com.draga.background.BackgroundLoader;
import com.draga.joystick.Joystick;
import com.draga.joystick.JoystickLoader;

public abstract class AssMan
{
    private static AssetManager GAME_ASSET_MANAGER;
    private static AssetManager ASSET_MANAGER;

    private static AssList ASSET_LIST;

    private AssMan()
    {
    }

    public static void create()
    {
        GAME_ASSET_MANAGER = new AssetManager();
        GAME_ASSET_MANAGER.setLoader(Joystick.class, new JoystickLoader());

        ASSET_MANAGER = new AssetManager();
        ASSET_MANAGER.setLoader(Background.class, new BackgroundLoader());

        ASSET_LIST = new Json().fromJson(
            AssList.class,
            Gdx.files.internal("assetList.json").readString());
    }

    public static void dispose()
    {
        GAME_ASSET_MANAGER.dispose();
        ASSET_MANAGER.dispose();
    }

    public static AssetManager getGameAssMan()
    {
        return GAME_ASSET_MANAGER;
    }

    public static AssetManager getAssMan()
    {
        return ASSET_MANAGER;
    }

    public static AssList getAssList()
    {
        return ASSET_LIST;
    }
}
