package com.draga.manager;

import com.draga.entity.GameEntity;

import java.util.LinkedList;
import java.util.Queue;

public class GameEntityManager
{
    private static final LinkedList<GameEntity> gameEntities          = new LinkedList<>();
    private static final Queue<GameEntity>      gameEntitiesToCreate  = new LinkedList<>();
    private static final Queue<GameEntity>      gameEntitiesToDestroy = new LinkedList<>();

    public static Queue<GameEntity> getGameEntitiesToCreate()
    {
        return gameEntitiesToCreate;
    }

    public static Queue<GameEntity> getGameEntitiesToDestroy()
    {
        return gameEntitiesToDestroy;
    }

    public static LinkedList<GameEntity> getGameEntities()
    {
        return gameEntities;
    }


    public static void dispose()
    {
        gameEntities.clear();
        gameEntitiesToCreate.clear();
        gameEntitiesToDestroy.clear();
    }
}
