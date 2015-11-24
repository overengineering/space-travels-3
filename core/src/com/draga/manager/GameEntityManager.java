package com.draga.manager;

import com.draga.entity.Explosion;
import com.draga.entity.GameEntity;
import com.google.common.base.Stopwatch;

import java.util.LinkedList;
import java.util.Queue;

public class GameEntityManager
{
    private static final LinkedList<GameEntity> GAME_ENTITIES           = new LinkedList<>();
    private static final Queue<GameEntity>      GAME_ENTITIES_TO_ADD    = new LinkedList<>();
    private static final Queue<GameEntity>      GAME_ENTITIES_TO_REMOVE = new LinkedList<>();

    public static LinkedList<GameEntity> getGameEntities()
    {
        return GAME_ENTITIES;
    }

    public static void reset()
    {
        GAME_ENTITIES.clear();
        GAME_ENTITIES_TO_ADD.clear();
        GAME_ENTITIES_TO_REMOVE.clear();
    }

    public static void update()
    {
         // Remove all game entities marked for removal.
        GAME_ENTITIES.removeAll(GAME_ENTITIES_TO_REMOVE);
        for (GameEntity gameEntity : GAME_ENTITIES_TO_REMOVE)
        {
            gameEntity.dispose();
        }
        GAME_ENTITIES_TO_REMOVE.clear();

        // Add game entities.
        GAME_ENTITIES.addAll(GAME_ENTITIES_TO_ADD);
        GAME_ENTITIES_TO_ADD.clear();
    }

    /**
     * dispose all the entities and empty the manager.
     */
    public static void dispose()
    {
        for (GameEntity gameEntity : GAME_ENTITIES)
        {
            gameEntity.dispose();
        }
        reset();
    }

    public static void removeGameEntity(GameEntity gameEntity)
    {
        GAME_ENTITIES_TO_REMOVE.add(gameEntity);
    }

    public static void addGameEntity(GameEntity gameEntity)
    {
        GAME_ENTITIES_TO_ADD.add(gameEntity);
    }
}
