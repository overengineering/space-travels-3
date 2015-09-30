package com.draga.entity;

import com.draga.BodyMaskBit;
import com.draga.entity.component.CircularPhysicComponent;
import com.draga.entity.component.TextureGraphicComponent;

public class Planet extends GameEntity
{
    public Planet(float mass, float radius, float x, float y, String texturePath)
    {
        physicComponent = new CircularPhysicComponent(
            mass, radius, x, y, BodyMaskBit.PLANET, BodyMaskBit.SHIP);
        graphicComponent = new TextureGraphicComponent(texturePath, physicComponent);
    }


    @Override public void update(float elapsed)
    {
        physicComponent.update(elapsed);
    }
}
