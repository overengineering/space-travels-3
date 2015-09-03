package com.draga.Ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.draga.Component.GraphicComponent;
import com.draga.Component.PhysicComponent;

public class ShipGraphicComponent extends GraphicComponent
{
    private PhysicComponent shipPhysicComponent;

    public ShipGraphicComponent(PhysicComponent shipPhysicComponent)
    {
        super("ship64.png");
        this.shipPhysicComponent = shipPhysicComponent;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(
            texture,
            shipPhysicComponent.getX(),
            shipPhysicComponent.getY(),
            shipPhysicComponent.getWith() / 2,
            shipPhysicComponent.getHeight() / 2,
            shipPhysicComponent.getWith(),
            shipPhysicComponent.getHeight(),
            1,
            1,
            shipPhysicComponent.getRotation(),
            0,
            0,
            shipPhysicComponent.getWith(),
            shipPhysicComponent.getHeight(),
            false,
            false);
    }
}
