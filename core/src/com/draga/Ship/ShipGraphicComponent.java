package com.draga.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.draga.Component.GraphicComponent;
import com.draga.Component.PhysicComponent;

public class ShipGraphicComponent extends GraphicComponent
{
    public ShipGraphicComponent(PhysicComponent shipPhysicComponent)
    {
        super("ship64.png", shipPhysicComponent);
    }
}
