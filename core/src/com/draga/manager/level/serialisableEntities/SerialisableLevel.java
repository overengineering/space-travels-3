package com.draga.manager.level.serialisableEntities;

import java.util.List;

public class SerialisableLevel
{
    // TODO: enforce name being unique?
    public String                   name;
    // TODO: float?
    public int                      width;
    public int                      height;
    public float                    fuel;
    public SerialisableBackground   serialisedBackground;
    public SerialisableShip         serialisedShip;
    public List<SerialisablePlanet> serialisedPlanets;
    public List<SerialisablePickup> serialisedPickups;

    public SerialisableLevel()
    {
    }
}
