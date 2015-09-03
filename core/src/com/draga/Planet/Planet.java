package com.draga.Planet;

import com.draga.GameEntity;

/**
 * Created by Administrator on 03/09/2015.
 */
public class Planet extends GameEntity
{
    public Planet(float mass, int diameter)
    {
        physicComponent = new PlanetPhysicComponent(mass, diameter);
    }
}
