package com.draga.spaceTravels3.event;

import com.badlogic.gdx.utils.Pool;

public class ScoreUpdatedEvent implements Pool.Poolable
{
    public String levelID;
    public String difficulty;

    public void set(String levelID, String difficulty)
    {
        this.levelID = levelID;
        this.difficulty = difficulty;
    }

    @Override
    public void reset()
    {
        this.levelID = null;
        this.difficulty = null;
    }
}
