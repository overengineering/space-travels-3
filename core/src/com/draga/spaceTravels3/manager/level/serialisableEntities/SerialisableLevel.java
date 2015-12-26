package com.draga.spaceTravels3.manager.level.serialisableEntities;

import java.util.List;

public class SerialisableLevel
{
    // TODO: JSON ignore?
    public String                   id;
    public String                   name;
    public float                    width;
    public float                    height;
    public float                    fuel;
    public float                    trajectorySeconds;
    public float                    maxLandingSpeed;
    public SerialisableBackground   serialisedBackground;
    public SerialisableShip         serialisedShip;
    public List<SerialisablePlanet> serialisedPlanets;
    public List<SerialisablePickup> serialisedPickups;

    public SerialisableLevel()
    {
    }
}
