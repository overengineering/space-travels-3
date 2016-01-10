package com.draga.spaceTravels3.manager;

import com.draga.PooledVector2;
import com.draga.background.Background;
import com.draga.spaceTravels3.BackgroundPositionController;

import java.util.Stack;

public abstract class BackgroundPositionManager
{
    private static Stack<BackgroundPositionController> backgroundPositionControllers;
    private static Background                          background;

    private BackgroundPositionManager()
    {
    }

    public static void create(Background background)
    {
        BackgroundPositionManager.background = background;
        BackgroundPositionManager.backgroundPositionControllers = new Stack<>();
    }

    public static void update(float deltaTime)
    {
        try (PooledVector2 movement = backgroundPositionControllers.peek().getMovement(deltaTime))
        {
            background.move(movement);
        }
    }

    public static void dispose()
    {
        backgroundPositionControllers.clear();
    }

    public static void addBackgroundPositionController(BackgroundPositionController backgroundPositionController)
    {
        BackgroundPositionManager.backgroundPositionControllers.push(backgroundPositionController);
    }

    public static void removeBackgroundPositionController(BackgroundPositionController backgroundPositionController)
    {
        BackgroundPositionManager.backgroundPositionControllers.remove(backgroundPositionController);
    }
}
