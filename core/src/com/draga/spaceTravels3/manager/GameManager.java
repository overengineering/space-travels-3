package com.draga.spaceTravels3.manager;

import com.badlogic.gdx.Game;

// TODO: maybe put in SpaceTravels3? That is the game itself so maybe just remove all this and
// always assume that ST3 is the game?
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
