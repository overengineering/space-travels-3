package com.draga.manager;

import com.badlogic.gdx.utils.Array;
import com.draga.entity.GameEntity;

public class GameEntityManager
{
    private static final Array<GameEntity> gameEntities = new Array<>();
    private static final Array<GameEntity> gameEntitiesToCreate = new Array<>();
    private static final Array<GameEntity> gameEntitiesToDestroy = new Array<>();

    public static void addGameEntityToCreate(GameEntity gameEntity)
    {
        gameEntitiesToCreate.add(gameEntity);
    }

    public static void addGameEntityToDestroy(GameEntity gameEntity)
    {
        gameEntitiesToDestroy.add(gameEntity);
    }

    public static Array<GameEntity> getGameEntitiesToCreate()
    {
        return gameEntitiesToCreate;
    }

    public static Array<GameEntity> getGameEntitiesToDestroy()
    {
        return gameEntitiesToDestroy;
    }

    public static Array<GameEntity> getGameEntities()
    {
        return gameEntities;
    }


    public static void dispose()
    {
        gameEntities.clear();
        gameEntitiesToCreate.clear();
        gameEntitiesToDestroy.clear();
    }

    public static void addGameEntity(GameEntity gameEntity)
    {
        gameEntities.add(gameEntity);
    }
}
