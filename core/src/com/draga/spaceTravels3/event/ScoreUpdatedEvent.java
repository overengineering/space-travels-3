package com.draga.spaceTravels3.event;

public class ScoreUpdatedEvent
{
    public String levelID;
    public String difficulty;
    public int    score;

    public ScoreUpdatedEvent(String levelID, String difficulty, int score)
    {
        this.levelID = levelID;
        this.difficulty = difficulty;
        this.score = score;
    }
}
