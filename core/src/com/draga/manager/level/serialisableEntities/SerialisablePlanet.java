package com.draga.manager.level.serialisableEntities;

/**
 * Created by Administrator on 05/09/2015.
 */
public class SerialisablePlanet
{
    String texturePath;
    int x;
    int y;
    int diameter;

    public SerialisablePlanet()
    {
    }

    public SerialisablePlanet(String texturePath, int x, int y, int diameter)
    {

        this.texturePath = texturePath;
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }
}
