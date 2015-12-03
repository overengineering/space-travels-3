package com.draga.manager;

import com.badlogic.gdx.Gdx;
import com.draga.Constants;
import com.draga.gameEntity.GameEntity;
import com.google.common.base.Joiner;

import java.util.LinkedList;
import java.util.Queue;

public class GameEntityManager
{
    private static final LinkedList<GameEntity> GAME_ENTITIES           = new LinkedList<>();
    private static final Queue<GameEntity>      GAME_ENTITIES_TO_ADD    = new LinkedList<>();
    private static final Queue<GameEntity>      GAME_ENTITIES_TO_REMOVE = new LinkedList<>();
    private static final String                 LOGGING_TAG             =
        GameEntityManager.class.getSimpleName();

    public static LinkedList<GameEntity> getGameEntities()
    {
        return GAME_ENTITIES;
    }

    public static void update()
    {
        if (Constants.IS_DEBUGGING)
        {
            if (!GAME_ENTITIES_TO_ADD.isEmpty())
            {
                Gdx.app.debug(
                    LOGGING_TAG,
                    "Game entities to add: " + Joiner.on(", ").join(GAME_ENTITIES_TO_ADD));
            }
            if (!GAME_ENTITIES_TO_REMOVE.isEmpty())
            {
                Gdx.app.debug(
                    LOGGING_TAG,
                    "Game entities to remove: " + Joiner.on(", ").join(GAME_ENTITIES_TO_REMOVE));
            }
        }

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

    public static void reset()
    {
        GAME_ENTITIES.clear();
        GAME_ENTITIES_TO_ADD.clear();
        GAME_ENTITIES_TO_REMOVE.clear();
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
