package com.draga.entity.ship;

import com.draga.entity.component.TextureGraphicComponent;
import com.draga.entity.component.PhysicComponent;

public class ShipGraphicComponent extends TextureGraphicComponent
{
    public ShipGraphicComponent(PhysicComponent shipPhysicComponent, String texturePath)
    {
        super(texturePath, shipPhysicComponent);
    }
}
