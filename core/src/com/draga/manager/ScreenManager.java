package com.draga.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.draga.Constants;

public class ScreenManager
{
    private static final String LOGGING_TAG = ScreenManager.class.getSimpleName();
    private static Game game;

    public static Game getGame()
    {
        return game;
    }

    public static void setGame(Game game)
    {
        ScreenManager.game = game;
    }

    public static Screen getActiveScreen()
    {
        return game.getScreen();
    }

    public static void setActiveScreen(Screen screen)
    {
        setActiveScreen(screen, true);
    }

    public static void setActiveScreen(Screen screen, boolean doDispose)
    {
        if (Constants.IS_DEBUGGING)
        {
            Gdx.app.debug(LOGGING_TAG, "Set active screen: " + screen.getClass().getSimpleName());
        }

        Screen activeScreen = game.getScreen();
        if (doDispose && activeScreen != null)
        {
            activeScreen.pause();
            activeScreen.dispose();
        }
        game.setScreen(screen);
    }
}
