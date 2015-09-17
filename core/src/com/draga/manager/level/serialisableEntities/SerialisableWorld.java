package com.draga.manager.level.serialisableEntities;

import java.util.List;

/**
 * Created by Administrator on 05/09/2015.
 */
public class SerialisableWorld
{
    public int width;
    public int height;
    public SerialisableBackground serialisedBackground;
    public SerialisableShip serialisedShip;
    public List<SerialisablePlanet> serialisedPlanets;

    public SerialisableWorld()
    {
    }
}
