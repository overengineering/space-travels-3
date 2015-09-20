package com.draga.ship;

import com.draga.component.GraphicComponent;
import com.draga.component.PhysicComponent;

public class ShipGraphicComponent extends GraphicComponent {
    public ShipGraphicComponent(PhysicComponent shipPhysicComponent, String texturePath) {
        super(texturePath, shipPhysicComponent);
    }
}
