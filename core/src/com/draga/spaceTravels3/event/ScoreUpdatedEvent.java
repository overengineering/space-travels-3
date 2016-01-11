package com.draga.spaceTravels3.event;

import com.badlogic.gdx.utils.Pool;

public class ScoreUpdatedEvent implements Pool.Poolable
{
    public String levelID;
    public String difficulty;
    public int    score;

    public void set(String levelID, String difficulty, int score)
    {
        this.levelID = levelID;
        this.difficulty = difficulty;
        this.score = score;
    }

    @Override
    public void reset()
    {
        this.levelID = null;
        this.difficulty = null;
        this.score = 0;
    }
}
