package com.draga.manager;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.draga.entity.GameEntity;

public class GameEntityManager implements Disposable
{
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


    @Override public void dispose()
    {
        gameEntitiesToCreate.clear();
        gameEntitiesToDestroy.clear();
    }
}
