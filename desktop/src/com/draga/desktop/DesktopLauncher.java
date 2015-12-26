package com.draga.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.draga.spaceTravels3.SpaceTravels3;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
//        config.width = 100;
//        config.height = 100;
//        config.fullscreen = true;
        config.fullscreen = false;
        config.resizable = false;

        new LwjglApplication(new SpaceTravels3(), config);
    }
}
