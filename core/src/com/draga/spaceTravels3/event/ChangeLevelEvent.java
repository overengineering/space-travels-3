package com.draga.spaceTravels3.event;

import com.draga.spaceTravels3.manager.level.serialisableEntities.SerialisableLevel;

public class ChangeLevelEvent
{
    public SerialisableLevel serialisableLevel;

    public ChangeLevelEvent(SerialisableLevel serialisableLevel)
    {
        this.serialisableLevel = serialisableLevel;
    }
}
