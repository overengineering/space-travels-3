package com.draga.manager.level.serialisableEntities;

import java.util.List;

public class SerialisableGameScene
{
    public int width;
    public int height;
    public SerialisableBackground serialisedBackground;
    public SerialisableShip serialisedShip;
    public List<SerialisablePlanet> serialisedPlanets;

    public SerialisableGameScene()
    {
    }
}