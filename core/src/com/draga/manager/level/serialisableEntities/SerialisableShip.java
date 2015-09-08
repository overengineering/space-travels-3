package com.draga.manager.level.serialisableEntities;

/**
 * Created by Administrator on 05/09/2015.
 */
public class SerialisableShip
{
    private String texturePath;

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
}
