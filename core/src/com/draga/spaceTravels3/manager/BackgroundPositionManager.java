package com.draga.spaceTravels3.manager;

import com.draga.PooledVector2;
import com.draga.background.Background;
import com.draga.spaceTravels3.BackgroundPositionController;

public abstract class BackgroundPositionManager
{
    private static BackgroundPositionController backgroundPositionController;
    private static Background                   background;

    private BackgroundPositionManager()
    {
    }

    public static void create(
        Background background,
        BackgroundPositionController backgroundPositionController)
    {
        BackgroundPositionManager.background = background;
        BackgroundPositionManager.backgroundPositionController = backgroundPositionController;
    }

    public static void update(float deltaTime)
    {
        backgroundPositionController.move(background, deltaTime);
    }

    public static void dispose()
    {

    }

    public static void setBackgroundPositionController(BackgroundPositionController backgroundPositionController)
    {
        BackgroundPositionManager.backgroundPositionController = backgroundPositionController;
    }
}
