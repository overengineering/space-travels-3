package com.draga.level.level.serialisableEntities;

/**
 * Created by Administrator on 05/09/2015.
 */
public class SerialisablePlanet
{
    String texturePath;
    int x;
    int y;
    int diameter;
    float mass;

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

    public float getMass()
    {
        return mass;
    }

    public void setMass(float mass)
    {
        this.mass = mass;
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

    public int getDiameter()
    {
        return diameter;
    }

    public void setDiameter(int diameter)
    {
        this.diameter = diameter;
    }
}
