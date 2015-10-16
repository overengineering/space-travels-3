package com.draga.manager.level.serialisableEntities;

import java.util.List;

public class SerialisableLevel
{
    public int                      width;
    public int                      height;
    public SerialisableBackground   serialisedBackground;
    public SerialisableShip         serialisedShip;
    public List<SerialisablePlanet> serialisedPlanets;
    public List<SerialisableStar>   serialisedStars;

    public SerialisableLevel()
    {
    }
}
