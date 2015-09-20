package com.draga.ship;

import com.draga.GameEntity;

public class Ship extends GameEntity {
    public ShipPhysicComponent physicComponent = null;

    public Ship(String texturePath) {
        physicComponent = new ShipPhysicComponent();
        inputComponent = new ShipInputComponent(physicComponent);
        graphicComponent = new ShipGraphicComponent(physicComponent, texturePath);
    }

    @Override
    public void update(float elapsed) {
        inputComponent.update(elapsed);
        physicComponent.update(elapsed);
    }
}
