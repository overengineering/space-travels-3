package com.draga.spaceTravels3.event;

import com.draga.spaceTravels3.manager.level.LevelPack;

public class ChangeLevelPackEvent
{
    public final LevelPack levelPack;

    public ChangeLevelPackEvent(LevelPack levelPack)
    {
        this.levelPack = levelPack;
    }
}
