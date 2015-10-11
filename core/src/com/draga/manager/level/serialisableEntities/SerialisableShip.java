package com.draga.manager.level.serialisableEntities;

public class SerialisableShip
{
    int x;
    int y;
    private String shipTexturePath;
    private String ThrusterTextureAtlasPath;

    public SerialisableShip()
    {

    }

    public SerialisableShip(String shipTexturePath)
    {

        this.shipTexturePath = shipTexturePath;
    }

    public String getThrusterTextureAtlasPath()
    {
        return ThrusterTextureAtlasPath;
    }

    public String getShipTexturePath()
    {
        return shipTexturePath;
    }

    public void setShipTexturePath(String shipTexturePath)
    {
        this.shipTexturePath = shipTexturePath;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}
