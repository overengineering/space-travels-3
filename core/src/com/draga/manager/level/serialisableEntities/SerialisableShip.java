package com.draga.manager.level.serialisableEntities;

/**
 * Created by Administrator on 05/09/2015.
 */
public class SerialisableShip
{
    private String texturePath;
    int x;
    int y;

    public SerialisableShip()
    {

    }

    public SerialisableShip(String texturePath)
    {

        this.texturePath = texturePath;
    }

    public String getTexturePath()
    {
        return texturePath;
    }

    public void setTexturePath(String texturePath)
    {
        this.texturePath = texturePath;
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
