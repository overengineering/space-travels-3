package com.draga.manager.level.serialisableEntities;

public class SerialisablePlanet
{
    String texturePath;
    int x;
    int y;
    float radius;
    float mass;

    public SerialisablePlanet()
    {

    }

    public SerialisablePlanet(String texturePath, int x, int y, int radius, float mass)
    {
        this.texturePath = texturePath;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
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

    public float getRadius()
    {
        return radius;
    }

    public void setRadius(int radius)
    {
        this.radius = radius;
    }
}
