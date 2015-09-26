package com.draga.entity.ship;

import com.draga.entity.component.GraphicComponent;
import com.draga.entity.component.PhysicComponent;

public class ShipGraphicComponent extends GraphicComponent {
    public ShipGraphicComponent(PhysicComponent shipPhysicComponent, String texturePath) {
        super(texturePath, shipPhysicComponent);
    }
}
