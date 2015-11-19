package com.draga.event;

import com.badlogic.gdx.utils.Pool;

public class ScoreEvent implements Pool.Poolable
{
    private int score;

    public int getScore()
    {

        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    @Override
    public void reset()
    {
        score = 0;
    }
}
