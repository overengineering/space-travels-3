package com.draga.manager;

import com.badlogic.gdx.Game;

public class GameManager
{
    private static final String LOGGING_TAG = GameManager.class.getSimpleName();
    private static Game game;

    public static Game getGame()
    {
        return game;
    }

    public static void setGame(Game game)
    {
        GameManager.game = game;
    }
}
