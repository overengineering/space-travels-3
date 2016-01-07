package com.draga.spaceTravels3.manager.level.serialisableEntities;

import java.util.LinkedHashMap;
import java.util.List;

public class SerialisableLevel
{
    // TODO: JSON ignore?
    public String                                        id;
    public String                                        name;
    public SerialisableShip                              serialisedShip;
    public List<SerialisablePlanet>                      serialisedPlanets;
    public List<SerialisablePickup>                      serialisedPickups;
    public LinkedHashMap<String, SerialisableDifficulty> serialisedDifficulties;

    public SerialisableLevel()
    {
    }
}
