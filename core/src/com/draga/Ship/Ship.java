package com.draga.ship;

import com.draga.GameEntity;
import com.draga.component.PhysicComponent;

public class Ship extends GameEntity {
    public ShipPhysicComponent physicComponent = null;

    public Ship(String texturePath) {
        physicComponent = new ShipPhysicComponent();
        inputComponent = new ShipInputComponent(physicComponent);
        graphicComponent = new ShipGraphicComponent(physicComponent, texturePath);
    }
}
