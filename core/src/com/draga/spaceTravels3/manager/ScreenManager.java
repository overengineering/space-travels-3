package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Gdx;
import com.draga.errorHandler.ErrorHandlerProvider;
import com.draga.spaceTravels3.ui.Screen;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public abstract class ScreenManager
{
    private static final String LOGGING_TAG = ScreenManager.class.getSimpleName();

    private static Stack<Screen>                      screens;
    private static LinkedList<Screen>                 screensToAdd;
    private static LinkedList<Screen>                 screensToRemove;
    private static HashMap<Screen, ArrayList<Screen>> screenBlockedScreens;

    public static void create()
    {
        screens = new Stack<>();
        screensToAdd = new LinkedList<>();
        screensToRemove = new LinkedList<>();
        screenBlockedScreens = new HashMap<>();
    }

    public static void addScreen(Screen screen)
    {
        screensToAdd.addLast(screen);
    }

    public static void removeScreen(Screen screen)
    {
        screensToRemove.addLast(screen);
    }

    public static void render(float deltaTime)
    {
        updateScreens();

        for (Screen screen : screens)
        {
            screen.render(deltaTime);
        }
    }

    private static void updateScreens()
    {
        if (!screensToRemove.isEmpty())
        {
            while (!screensToRemove.isEmpty())
            {
                Screen screenToRemove = screensToRemove.removeFirst();
                Gdx.app.debug(LOGGING_TAG, "Removing " + screenToRemove.getClass().getSimpleName());

                if (screens.contains(screenToRemove))
                {
                    if (screenBlockedScreens.containsKey(screenToRemove))
                    {
                        ArrayList<Screen> screensToUnblock =
                            screenBlockedScreens.get(screenToRemove);
                        Gdx.app.debug(
                            LOGGING_TAG,
                            "Unblocking " + Joiner.on(", ").join(screensToUnblock));
                        for (Screen screenToUnblock : screensToUnblock)
                        {
                            screenToUnblock.show();
                            ScreenManager.screens.add(screenToUnblock);
                        }
                        screenBlockedScreens.remove(screenToRemove);
                    }
                    screens.remove(screenToRemove);
                    screenToRemove.hide();
                    screenToRemove.dispose();
                }
                else
                {
                    ErrorHandlerProvider.handle(
                        LOGGING_TAG,
                        "Trying to remove "
                            + screenToRemove.getClass()
                            + " that is not in the list!");
                }
            }
            screensToRemove.clear();

            screens.peek().show();
        }

        while (!screensToAdd.isEmpty())
        {
            Screen screenToAdd = screensToAdd.removeFirst();
            Gdx.app.debug(LOGGING_TAG, "Adding " + screenToAdd.getClass().getSimpleName());

            screenToAdd.show();

            if (screenToAdd.blockParents())
            {
                ArrayList<Screen> blockedScreens = new ArrayList<>();
                for (Screen screen : screens)
                {
                    if (screen.isBlockable())
                    {
                        screen.hide();
                        blockedScreens.add(screen);
                    }
                }
                if (!blockedScreens.isEmpty())
                {
                    Gdx.app.debug(LOGGING_TAG, "Blocking " + Joiner.on(", ").join(blockedScreens));
                    screens.removeAll(blockedScreens);
                    screenBlockedScreens.put(screenToAdd, blockedScreens);
                }
            }

            screens.add(screenToAdd);
        }
        screensToAdd.clear();
    }

    public static void dispose()
    {
        for (Screen screen : screens)
        {
            screen.dispose();
        }
    }

    public static void resume()
    {
        if (!screens.empty())
        {
            screens.peek().resume();
        }
    }

    public static void pause()
    {
        if (!screens.empty())
        {
            screens.peek().pause();
        }
    }
}