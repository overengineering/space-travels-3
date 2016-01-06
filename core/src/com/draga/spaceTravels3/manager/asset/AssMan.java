package com.draga.spaceTravels3.manager.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Json;
import com.draga.background.Background;
import com.draga.background.BackgroundLoader;
import com.draga.joystick.Joystick;
import com.draga.joystick.JoystickLoader;

public class AssMan
{
    private static AssetManager ASSET_MANAGER;
    private static AssList      ASSET_LIST;

    public static void create()
    {
        ASSET_MANAGER = new AssetManager();
        ASSET_MANAGER.setLoader(Background.class, new BackgroundLoader());
        ASSET_MANAGER.setLoader(Joystick.class, new JoystickLoader());

        ASSET_LIST = new Json().fromJson(
            AssList.class,
            Gdx.files.internal("assetList.json").readString());
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
        return ASSET_LIST;
    }
}
