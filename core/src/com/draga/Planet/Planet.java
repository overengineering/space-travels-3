package com.draga.Planet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.draga.Component.CircularPhysicComponent;
import com.draga.Component.GraphicComponent;
import com.draga.GameEntity;

/**
 * Created by Administrator on 03/09/2015.
 */
public class Planet extends GameEntity
{
    public Planet(float mass, int diameter, int x, int y, String texturePath)
    {
        physicComponent = new CircularPhysicComponent(mass, diameter, x, y);
        graphicComponent = new GraphicComponent(texturePath, physicComponent);
    }
}
