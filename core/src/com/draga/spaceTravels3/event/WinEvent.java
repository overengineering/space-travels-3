package com.draga.spaceTravels3.event;

import com.draga.spaceTravels3.Score;

public class WinEvent
{
    public Score   score;
    public Integer previousBestScore;

    public WinEvent(Score score, Integer previousBestScore)
    {
        this.score = score;
        this.previousBestScore = previousBestScore;
    }
}
